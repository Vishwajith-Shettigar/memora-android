package com.example.util

class UsernameAlreadyExistsException : Exception("Username already exists.")
class EmailAlreadyExistsException : Exception("Email already exists.")
class UnverifiedEmailException : Exception("Please verify your email.")
class EmailDoesntExistException : Exception("Account doesn't exists.")
class PasswordDoesntMatchException : Exception("Password Doesn't match.")
class UnspecifiedException : Exception("Something went wrong, please try again")
class AskDetailsException : Exception("")
class NoAuthException : Exception("")

