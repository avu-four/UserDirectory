package com.example.userdirectory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.userdirectory.data.User
import com.example.userdirectory.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class UserUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = ""
)

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<UserUiState> = combine(
        _searchQuery.flatMapLatest { query ->
            if (query.isEmpty()) {
                repository.getAllUsers()
            } else {
                repository.searchUsers(query)
            }
        },
        _isLoading,
        _errorMessage
    ) { users, loading, error ->
        UserUiState(
            users = users,
            isLoading = loading,
            errorMessage = error,
            searchQuery = _searchQuery.value
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserUiState()
    )

    init {
        refreshUsers()
    }

    fun refreshUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.refreshUsers()
                .onSuccess {
                    _errorMessage.value = null
                }
                .onFailure { e ->
                    _errorMessage.value = "Failed to refresh: ${e.message}"
                }

            _isLoading.value = false
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun clearError() {
        _errorMessage.value = null
    }
}