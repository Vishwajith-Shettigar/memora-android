package com.example.timecapsule.ui.theme.fakedata

import com.example.timecapsule.R

data class User(
  val name: String,
  val username: String,
  val imageResId: Int
)

// Creating a mutable list of User objects with unique usernames and names
val userList = mutableListOf(
  User(
    username = "john_doe",
    name = "John Doe",
    imageResId = R.drawable.testimg1
  ),
  User(
    username = "jane_smith",
    name = "Jane Smith",
    imageResId = R.drawable.testimg2
  ),
  User(
    username = "alex_jones",
    name = "Alex Jones",
    imageResId = R.drawable.testimg3
  ),
  User(
    username = "emily_clark",
    name = "Emily Clark",
    imageResId = R.drawable.testimg4
  ),
  User(
    username = "michael_brown",
    name = "Michael Brown",
    imageResId = R.drawable.testimg5
  ),
  User(
    username = "sarah_lee",
    name = "Sarah Lee",
    imageResId = R.drawable.testimg6
  ),
  User(
    username = "david_wilson",
    name = "David Wilson",
    imageResId = R.drawable.testimg7
  ),
  User(
    username = "linda_martin",
    name = "Linda Martin",
    imageResId = R.drawable.testimg8
  ),
  User(
    username = "chris_taylor",
    name = "Chris Taylor",
    imageResId = R.drawable.testimg9
  )
)
