package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Article
import com.example.data.model.Child
import com.example.data.model.ParentUser
import com.example.data.model.WeightRecord
import com.example.data.repository.KmsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KmsViewModel(private val repository: KmsRepository) : ViewModel() {

    // Current logged-in parent
    private val _currentParent = MutableStateFlow<ParentUser?>(null)
    val currentParent: StateFlow<ParentUser?> = _currentParent.asStateFlow()

    // Login screen states
    val loginUsername = MutableStateFlow("")
    val loginPin = MutableStateFlow("")
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError = _loginError.asStateFlow()

    // Registration screen states
    val regUsername = MutableStateFlow("")
    val regName = MutableStateFlow("")
    val regPin = MutableStateFlow("")
    private val _regError = MutableStateFlow<String?>(null)
    val regError = _regError.asStateFlow()
    private val _regSuccess = MutableStateFlow(false)
    val regSuccess = _regSuccess.asStateFlow()

    // Load parent's children adaptively
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val childrenList: StateFlow<List<Child>> = _currentParent
        .flatMapLatest { parent ->
            if (parent != null) {
                repository.getChildrenForParent(parent.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected child for tracking weight entries & KMS chart
    private val _selectedChild = MutableStateFlow<Child?>(null)
    val selectedChild: StateFlow<Child?> = _selectedChild.asStateFlow()

    // Weight records load adaptively as well based on selected child
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val weightRecords: StateFlow<List<WeightRecord>> = _selectedChild
        .flatMapLatest { child ->
            if (child != null) {
                repository.getWeightRecordsForChild(child.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Child form states
    val childName = MutableStateFlow("")
    val childBirthDate = MutableStateFlow("")
    val childGender = MutableStateFlow("Perempuan") // or "Laki-laki"
    private val _childFormError = MutableStateFlow<String?>(null)
    val childFormError = _childFormError.asStateFlow()

    // Weight form states
    val weightDate = MutableStateFlow("")
    val weightAgeMonths = MutableStateFlow("")
    val weightValue = MutableStateFlow("")
    val weightNotes = MutableStateFlow("")
    private val _weightFormError = MutableStateFlow<String?>(null)
    val weightFormError = _weightFormError.asStateFlow()

    // Favorites mapping
    val favoriteArticleIds: StateFlow<Set<String>> = repository.getAllFavorites()
        .map { favList -> favList.map { it.articleId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    // Selected Article for detail screen
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun toggleFavorite(articleId: String) {
        viewModelScope.launch {
            val favs = favoriteArticleIds.value
            if (favs.contains(articleId)) {
                repository.deleteFavorite(articleId)
            } else {
                repository.insertFavorite(articleId)
            }
        }
    }

    fun selectChild(child: Child?) {
        _selectedChild.value = child
    }

    // Set sample data for first launch onboarding if requested or automatically to populate nicely
    fun setupSampleDataForDemo(parent: ParentUser) {
        viewModelScope.launch {
            val defaultChild = Child(
                parentId = parent.id,
                name = "Aisyah Putri",
                birthDate = "12/03/2023",
                gender = "Perempuan"
            )
            val cid = repository.insertChild(defaultChild)
            
            // Add some historical weight entries showing growth
            // 0 months: 3.2 kg
            // 3 months: 5.6 kg
            // 6 months: 7.2 kg
            // 9 months: 8.5 kg
            // 12 months: 9.2 kg
            repository.insertWeightRecord(WeightRecord(childId = cid, date = "12/03/2023", ageInMonths = 0, weight = 3.2, notes = "Lahir sehat normal"))
            repository.insertWeightRecord(WeightRecord(childId = cid, date = "12/06/2023", ageInMonths = 3, weight = 5.6, notes = "Rutin imunisasi"))
            repository.insertWeightRecord(WeightRecord(childId = cid, date = "12/09/2023", ageInMonths = 6, weight = 7.2, notes = "Ada MPASI perdana"))
            repository.insertWeightRecord(WeightRecord(childId = cid, date = "12/12/2023", ageInMonths = 9, weight = 8.5, notes = "Suka bubur tim"))
            repository.insertWeightRecord(WeightRecord(childId = cid, date = "12/03/2024", ageInMonths = 12, weight = 9.2, notes = "Sehat, nafsu makan baik"))
            
            // Set newly created child as active selection
            _selectedChild.value = defaultChild.copy(id = cid)
        }
    }

    // Parental Login action
    fun loginParent(onSuccess: () -> Unit) {
        val user = loginUsername.value.trim().lowercase()
        val pin = loginPin.value.trim()

        if (user.isEmpty() || pin.isEmpty()) {
            _loginError.value = "Username dan PIN tidak boleh kosong."
            return
        }

        viewModelScope.launch {
            val foundParent = repository.getParentByUsername(user)
            if (foundParent == null) {
                _loginError.value = "Username tidak terdaftar."
            } else if (foundParent.pin != pin) {
                _loginError.value = "PIN tidak sesuai."
            } else {
                _loginError.value = null
                _currentParent.value = foundParent
                // Select first child if any
                onSuccess()
            }
        }
    }

    // Parent Registration
    fun registerParent(onSuccess: () -> Unit) {
        val user = regUsername.value.trim().lowercase()
        val name = regName.value.trim()
        val pin = regPin.value.trim()

        if (user.isEmpty() || name.isEmpty() || pin.isEmpty()) {
            _regError.value = "Semua bidang pendaftaran wajib diisi."
            return
        }

        if (pin.length < 4) {
            _regError.value = "PIN minimal memiliki 4 karakter/angka."
            return
        }

        viewModelScope.launch {
            val existing = repository.getParentByUsername(user)
            if (existing != null) {
                _regError.value = "Username sudah digunakan, silakan pilih yang lain."
                return@launch
            }

            val newParent = ParentUser(username = user, name = name, pin = pin)
            val parentId = repository.registerParent(newParent)
            val parentWithId = newParent.copy(id = parentId)
            
            // Log in newly registered user
            _currentParent.value = parentWithId
            _selectedChild.value = null
            _regError.value = null
            _regSuccess.value = true
            
            // Prepopulate database with a sample child for the demo so it looks exactly like the mockup out of the box!
            setupSampleDataForDemo(parentWithId)

            onSuccess()
        }
    }

    // Log out parent
    fun logout() {
        _currentParent.value = null
        _selectedChild.value = null
        loginUsername.value = ""
        loginPin.value = ""
        regUsername.value = ""
        regName.value = ""
        regPin.value = ""
        _loginError.value = null
        _regError.value = null
        _regSuccess.value = false
    }

    // Save Child Form
    fun saveChild(onSuccess: () -> Unit) {
        val parent = _currentParent.value ?: return
        val name = childName.value.trim()
        val dob = childBirthDate.value.trim()
        val gender = childGender.value

        if (name.isEmpty() || dob.isEmpty()) {
            _childFormError.value = "Nama dan Tanggal Lahir anak harus diisi."
            return
        }

        viewModelScope.launch {
            val newChild = Child(
                parentId = parent.id,
                name = name,
                birthDate = dob,
                gender = gender
            )
            val newId = repository.insertChild(newChild)
            // Auto select
            _selectedChild.value = newChild.copy(id = newId)
            
            // Clear inputs
            childName.value = ""
            childBirthDate.value = ""
            childGender.value = "Perempuan"
            _childFormError.value = null
            onSuccess()
        }
    }

    // Delete Child
    fun deleteChild(child: Child) {
        viewModelScope.launch {
            repository.deleteChild(child)
            if (_selectedChild.value?.id == child.id) {
                _selectedChild.value = null
            }
        }
    }

    // Save Weight Form
    fun saveWeightRecord(onSuccess: () -> Unit) {
        val child = _selectedChild.value
        if (child == null) {
            _weightFormError.value = "Silakan daftarkan atau pilih profil anak terlebih dahulu."
            return
        }

        val date = weightDate.value.trim()
        val ageStr = weightAgeMonths.value.trim()
        val weightStr = weightValue.value.trim()
        val notes = weightNotes.value.trim()

        if (date.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty()) {
            _weightFormError.value = "Tanggal, Umur, dan Berat Badan harus diisi."
            return
        }

        val age = ageStr.toIntOrNull()
        if (age == null || age < 0) {
            _weightFormError.value = "Umur anak tidak valid."
            return
        }

        val weight = weightStr.toDoubleOrNull()
        if (weight == null || weight <= 0.0) {
            _weightFormError.value = "Berat badan anak tidak valid."
            return
        }

        viewModelScope.launch {
            val newRecord = WeightRecord(
                childId = child.id,
                date = date,
                ageInMonths = age,
                weight = weight,
                notes = notes
            )
            repository.insertWeightRecord(newRecord)

            // Clear inputs
            weightDate.value = ""
            weightAgeMonths.value = ""
            weightValue.value = ""
            weightNotes.value = ""
            _weightFormError.value = null
            onSuccess()
        }
    }

    // Delete Weight Record
    fun deleteWeightRecord(record: WeightRecord) {
        viewModelScope.launch {
            repository.deleteWeightRecord(record)
        }
    }
}

class KmsViewModelFactory(private val repository: KmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KmsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KmsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
