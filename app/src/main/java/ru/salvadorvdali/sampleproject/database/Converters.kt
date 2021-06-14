package ru.salvadorvdali.sampleproject.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import retrofit2.converter.gson.GsonConverterFactory
import ru.salvadorvdali.sampleproject.models.Address
import ru.salvadorvdali.sampleproject.models.Company

class AddressConverter{
    @TypeConverter
    fun fromAddressToString(address: Address):String{
        var gson = Gson().toJson(address)
        return gson
    }

    @TypeConverter
    fun fromStringToAddress(address: String): Address{
return Gson().fromJson<Address>(address, Address::class.java)
    }
}

class CompanyConverter{
    @TypeConverter
    fun fromCompanyToString(company: Company):String{
        var gson = Gson().toJson(company)
        return gson
    }

    @TypeConverter
    fun fromStringToCompany(company: String): Company{
        return Gson().fromJson(company, Company::class.java)
    }
}