package com.demo.alex.domain

import org.springframework.data.repository.CrudRepository

interface CommentRepository: CrudRepository<Comment, String> {
}