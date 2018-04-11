package com.sjkj.parent.exception

import android.accounts.NetworkErrorException

/**
 * @author by dingl on 2017/9/30.
 * @desc RequestException
 */
class RequestDataException(message: String) : Exception(message)

class RequestFailedException(message: String) : Exception(message)

class RequestIllegalArgumentException(message: String) : IllegalArgumentException(message)

class RequestNetWorkException(message: String) : NetworkErrorException(message)

class RequestNullDataException(message: String) : Exception(message)
