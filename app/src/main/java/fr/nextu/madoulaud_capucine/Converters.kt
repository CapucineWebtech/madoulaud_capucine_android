package fr.nextu.madoulaud_capucine

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {
    companion object {
        private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

        @TypeConverter
        @JvmStatic
        fun toDate(dateString: String?): Date? {
            return dateString?.let { format.parse(it) }
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): String? {
            return date?.let { format.format(it) }
        }
    }
}
