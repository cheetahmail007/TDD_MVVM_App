package com.sampleNote.work.model.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.sampleNote.work.model.Note
/*
Database class that create instance for room db of Note
*/
@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun getNoteDao(): NoteDao
}