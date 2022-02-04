package com.harman.contacts.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.harman.contacts.BuildConfig
import com.harman.contacts.ui.contacts.ContactListViewModel
import com.harman.contacts.ui.editcontact.EditContactViewModel
import com.harman.contacts.ui.users.GithubUsersViewModel
import com.harman.contacts.ui.viewcontact.ViewContactViewModel
import com.harman.domain.repository.ContactsRepository
import com.harman.data.db.ContactDao
import com.harman.data.db.ContactsDb
import com.harman.data.filesystem.FileSystemService
import com.harman.data.net.GithubUsersAPI
import com.harman.domain.interactor.*
import com.harman.domain.repository.UsersRepository
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val appModule = module {
    single<ContactsRepository> { com.harman.data.repository.ContactsRepository(get(), get()) }
    single<UsersRepository> { com.harman.data.repository.UsersRepository(get()) }
    single<ContactsDb> {
        Room
            .databaseBuilder(androidContext(), ContactsDb::class.java, "contacts.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<ContactDao> { get<ContactsDb>().contactDao() }
    single<FileSystemService> { FileSystemService(androidContext()) }
}

val networkModule = module {
    single<Cache> { Cache(androidContext().cacheDir, CACHE_MAX_SIZE_BYTES) }
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .cache(get())
            .connectTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .build()
    }
    single<Gson> { GsonBuilder().serializeNulls().create() }
    single<GsonConverterFactory> { GsonConverterFactory.create(get()) }
    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .addConverterFactory(get<GsonConverterFactory>())
            .baseUrl(BuildConfig.ENDPOINT)
            .build()
    }
    single { get<Retrofit>().create(GithubUsersAPI::class.java) }
}

private const val TIMEOUT_MILLIS = 30000L
private const val CACHE_MAX_SIZE_BYTES = 1024L

val viewModelModule = module {
    viewModel { ContactListViewModel(get(), get(), get(), get()) }
    viewModel { EditContactViewModel(get(), get()) }
    viewModel { ViewContactViewModel(get(), get()) }
    viewModel { GithubUsersViewModel(get()) }
}

val useCasesModule = module {
    single { DeleteContact(get()) }
    single { ExportContacts(get()) }
    single { GetContactById(get()) }
    single { GetContacts(get()) }
    single { GetContactsFiltered(get()) }
    single { ImportContacts(get()) }
    single { SaveContact(get()) }
    single { GetUsers(get()) }
}


val allModules = appModule + viewModelModule + useCasesModule + networkModule