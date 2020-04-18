package a.com.fpolyshop.data.source.local

import a.com.fpolyshop.data.source.DataSource

class LocalDataSource private constructor() :
    DataSource.Local {


    companion object {
        private var instance: LocalDataSource? = null
        fun getInstance() =
            instance ?: LocalDataSource().also { instance = it }
    }
}
