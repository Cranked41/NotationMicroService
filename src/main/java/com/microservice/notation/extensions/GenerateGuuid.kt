package com.microservice.notation.extensions

import java.util.UUID

object GenerateGuuid {

    fun getRandomGuid(): Long {
        val uuid = UUID.randomUUID()
        val mostSignificantBits = uuid.mostSignificantBits
        val leastSignificantBits = uuid.leastSignificantBits

        // 128 bit UUID'yi 64 bit long'a dönüştürme
        return mostSignificantBits xor leastSignificantBits
    }

}

