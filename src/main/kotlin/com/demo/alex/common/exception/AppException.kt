package com.demo.alex.common.exception

import java.lang.RuntimeException

class AppException {

    class NotFoundException(message: String) : RuntimeException(message)
}