package com.example.util

class UsernameAlreadyExistsException : Exception("Username already exists.")
class EmailAlreadyExistsException : Exception("Email already exists.")
class UnspecifiedException : Exception("Something went wrong, please try again")