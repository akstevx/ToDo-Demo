package com.qucoon.todoapp.data.di

import androidx.room.Room
import android.content.Context
import com.qucoon.todoapp.data.Constants
import com.qucoon.todoapp.data.ItemRepositoryImpl
import com.qucoon.todoapp.data.database.ItemRoomDatabase
import com.qucoon.todoapp.domain.repository.ItemRepository
import com.qucoon.todoapp.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    //Database dependency
    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String {
        return Constants.DATABASE_NAME
    }

    @Provides
    @Singleton
    fun provideAppDatabase(  @ApplicationContext context: Context): ItemRoomDatabase {
        return Room.databaseBuilder(context, ItemRoomDatabase::class.java, provideDatabaseName()).fallbackToDestructiveMigration()
                .build()
    }

    //Repository dependency
    @Provides
    @Singleton
    fun provideItemRepository(appDatabase: ItemRoomDatabase): ItemRepository {
        return ItemRepositoryImpl(appDatabase)
    }

    //UseCase dependencies
    @Provides
    @Singleton
    fun provideItemsUseCase(itemRepository: ItemRepository):GetItemsUseCase {
        return GetItemsUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideSearchItemUseCase(itemRepository: ItemRepository): SearchItemUseCase {
        return SearchItemUseCase(itemRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteItemUseCaseImpl(itemRepository: ItemRepository): DeleteItemUseCase {
        return DeleteItemUseCaseImpl(itemRepository)
    }

    @Provides
    @Singleton
    fun provideEditItemUseCaseImplImpl(itemRepository: ItemRepository): EditItemUseCase {
        return EditItemUseCaseImpl(itemRepository)
    }
}
