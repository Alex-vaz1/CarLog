package com.example.carlogger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.carlogger.data.dao.TripDao
import com.example.carlogger.data.dao.UserDao
import com.example.carlogger.data.dao.FuelEntryDao
import com.example.carlogger.data.model.FuelEntry
import com.example.carlogger.data.model.Trip
import com.example.carlogger.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Database(
    entities = [FuelEntry::class, Trip::class, User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun fuelEntryDao(): FuelEntryDao
    abstract fun tripDao(): TripDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "carlogger_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(PrepopulateCallback())
                    .build()
                    .also { INSTANCE = it }
            }
    }

    private class PrepopulateCallback : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Inserta todos los trips iniciales en segundo plano
            val dao = INSTANCE!!.tripDao()
            CoroutineScope(Dispatchers.IO).launch {
                dao.clearAll()
                initialTrips().forEach { dao.insert(it) }
            }
        }

        private fun initialTrips(): List<Trip> {
            val formatter = DateTimeFormatter.ofPattern("d/M/yy")
            val zone = ZoneId.systemDefault()
            fun parse(dateStr: String): Instant =
                LocalDate.parse(dateStr, formatter)
                    .atStartOfDay(zone)
                    .toInstant()

            return listOf(
                Trip(userId = "G", odometer = 280664, distanceTraveled = 55,  price = 602.0,  date = parse("27/12/23")),
                Trip(userId = "G", odometer = 280685, distanceTraveled = 21,  price = 229.9,  date = parse("27/12/23")),
                Trip(userId = "G", odometer = 280698, distanceTraveled = 13,  price = 142.3,  date = parse("28/12/23")),
                Trip(userId = "G", odometer = 280702, distanceTraveled = 4,   price = 43.8,   date = parse("29/12/23")),
                Trip(userId = "G", odometer = 280716, distanceTraveled = 14,  price = 153.2,  date = parse("29/12/23")),
                Trip(userId = "G", odometer = 280728, distanceTraveled = 12,  price = 131.4,  date = parse("29/12/23")),
                Trip(userId = "A", odometer = 280770, distanceTraveled = 42,  price = 459.7,  date = parse("30/12/23")),
                Trip(userId = "A", odometer = 280778, distanceTraveled = 8,   price = 87.6,   date = parse("31/12/23")),
                Trip(userId = "A", odometer = 280804, distanceTraveled = 26,  price = 284.6,  date = parse("2/1/24")),
                Trip(userId = "A", odometer = 280808, distanceTraveled = 4,   price = 43.8,   date = parse("3/1/24")),
                Trip(userId = "A", odometer = 280821, distanceTraveled = 13,  price = 142.3,  date = parse("7/1/24")),
                Trip(userId = "G", odometer = 280865, distanceTraveled = 44,  price = 481.6,  date = parse("14/1/24")),
                Trip(userId = "A", odometer = 280877, distanceTraveled = 12,  price = 131.4,  date = parse("15/1/24")),
                Trip(userId = "G", odometer = 280886, distanceTraveled = 9,   price = 98.5,   date = parse("15/1/24")),
                Trip(userId = "A", odometer = 280899, distanceTraveled = 13,  price = 142.3,  date = parse("16/1/24")),
                Trip(userId = "G", odometer = 280910, distanceTraveled = 11,  price = 120.4,  date = parse("17/1/24")),
                Trip(userId = "A", odometer = 280924, distanceTraveled = 14,  price = 153.2,  date = parse("17/1/24")),
                Trip(userId = "G", odometer = 280937, distanceTraveled = 13,  price = 142.3,  date = parse("18/1/24")),
                Trip(userId = "A", odometer = 280950, distanceTraveled = 13,  price = 142.3,  date = parse("18/1/24")),
                Trip(userId = "G", odometer = 280970, distanceTraveled = 20,  price = 218.9,  date = parse("22/1/24")),
                Trip(userId = "G", odometer = 281010, distanceTraveled = 40,  price = 437.8,  date = parse("26/1/24")),
                Trip(userId = "A", odometer = 281017, distanceTraveled = 7,   price = 76.6,   date = parse("28/1/24")),
                Trip(userId = "G", odometer = 281094, distanceTraveled = 77,  price = 842.8,  date = parse("2/2/24")),
                Trip(userId = "A", odometer = 281098, distanceTraveled = 4,   price = 43.8,   date = parse("6/2/24")),
                Trip(userId = "G", odometer = 281149, distanceTraveled = 51,  price = 558.3,  date = parse("9/2/24")),
                Trip(userId = "G", odometer = 281193, distanceTraveled = 44,  price = 481.6,  date = parse("25/2/24")),
                Trip(userId = "A", odometer = 281207, distanceTraveled = 14,  price = 153.2,  date = parse("25/2/24")),
                Trip(userId = "G", odometer = 281213, distanceTraveled = 6,   price = 65.7,   date = parse("27/2/24")),
                Trip(userId = "AG",odometer = 281217, distanceTraveled = 4,   price = 43.8,   date = parse("27/2/24")),
                Trip(userId = "G", odometer = 281239, distanceTraveled = 22,  price = 240.8,  date = parse("3/3/24")),
                Trip(userId = "A", odometer = 281251, distanceTraveled = 12,  price = 131.4,  date = parse("3/3/24")),
                Trip(userId = "G", odometer = 281271, distanceTraveled = 20,  price = 218.9,  date = parse("6/3/24")),
                Trip(userId = "A", odometer = 281299, distanceTraveled = 28,  price = 306.5,  date = parse("6/3/24")),
                Trip(userId = "G", odometer = 281328, distanceTraveled = 29,  price = 317.4,  date = parse("8/3/24")),
                Trip(userId = "AG",odometer = 281346, distanceTraveled = 18,  price = 197.0,  date = parse("12/3/24")),
                Trip(userId = "G", odometer = 281396, distanceTraveled = 50,  price = 547.3,  date = parse("12/3/24")),
                Trip(userId = "G", odometer = 281446, distanceTraveled = 50,  price = 547.3,  date = parse("16/3/24")),
                Trip(userId = "A", odometer = 281459, distanceTraveled = 13,  price = 142.3,  date = parse("17/3/24")),
                Trip(userId = "G", odometer = 281527, distanceTraveled = 68,  price = 744.3,  date = parse("25/3/24")),
                Trip(userId = "A", odometer = 281530, distanceTraveled = 3,   price = 32.8,   date = parse("25/3/24")),
                Trip(userId = "G", odometer = 281598, distanceTraveled = 68,  price = 744.3,  date = parse("31/3/24")),
                Trip(userId = "AG",odometer = 281606, distanceTraveled = 8,   price = 87.6,   date = parse("31/3/24")),
                Trip(userId = "A", odometer = 281629, distanceTraveled = 23,  price = 251.8,  date = parse("1/4/24")),
                Trip(userId = "G", odometer = 281740, distanceTraveled = 111, price = 1215.0, date = parse("6/4/24")),
                Trip(userId = "A", odometer = 281751, distanceTraveled = 11,  price = 120.4,  date = parse("6/4/24")),
                Trip(userId = "G", odometer = 281980, distanceTraveled = 229, price = 2506.7,date = parse("21/4/24")),
                Trip(userId = "A", odometer = 281992, distanceTraveled = 12,  price = 131.4,  date = parse("21/4/24")),
                Trip(userId = "G", odometer = 282065, distanceTraveled = 73,  price = 799.1,  date = parse("26/4/24")),
                Trip(userId = "A", odometer = 282076, distanceTraveled = 11,  price = 120.4,  date = parse("27/4/24")),
                Trip(userId = "G", odometer = 282094, distanceTraveled = 18,  price = 197.0,  date = parse("29/4/24")),
                Trip(userId = "A", odometer = 282106, distanceTraveled = 12,  price = 131.4,  date = parse("29/4/24")),
                Trip(userId = "G", odometer = 282134, distanceTraveled = 28,  price = 306.5,  date = parse("1/5/24")),
                Trip(userId = "A", odometer = 282146, distanceTraveled = 12,  price = 131.4,  date = parse("1/5/24")),
                Trip(userId = "G", odometer = 282165, distanceTraveled = 19,  price = 208.0,  date = parse("2/5/24")),
                Trip(userId = "AG",odometer = 282172, distanceTraveled = 7,   price = 76.6,   date = parse("2/5/24")),
                Trip(userId = "G", odometer = 282217, distanceTraveled = 45,  price = 492.6,  date = parse("8/5/24")),
                Trip(userId = "A", odometer = 282226, distanceTraveled = 9,   price = 98.5,   date = parse("9/5/24")),
                Trip(userId = "A", odometer = 282249, distanceTraveled = 23,  price = 251.8,  date = parse("10/5/24")),
                Trip(userId = "G", odometer = 282342, distanceTraveled = 93,  price = 1018.0, date = parse("21/5/24")),
                Trip(userId = "A", odometer = 282345, distanceTraveled = 3,   price = 32.8,   date = parse("21/5/24")),
                Trip(userId = "G", odometer = 282484, distanceTraveled = 139, price = 1521.5,date = parse("1/6/24")),
                Trip(userId = "A", odometer = 282493, distanceTraveled = 9,   price = 98.5,   date = parse("1/6/24")),
                Trip(userId = "G", odometer = 282608, distanceTraveled = 115, price = 1258.8,date = parse("9/6/24")),
                Trip(userId = "AG",odometer = 282615, distanceTraveled = 7,   price = 76.6,   date = parse("9/6/24")),
                Trip(userId = "G", odometer = 282850, distanceTraveled = 235, price = 2572.3,date = parse("24/6/24")),
                Trip(userId = "G", odometer = 282912, distanceTraveled = 62,  price = 678.7,  date = parse("26/6/24")),
                Trip(userId = "A", odometer = 282925, distanceTraveled = 13,  price = 142.3,  date = parse("26/6/24")),
                Trip(userId = "G", odometer = 282982, distanceTraveled = 57,  price = 623.9,  date = parse("30/6/24")),
                Trip(userId = "A", odometer = 282989, distanceTraveled = 7,   price = 76.6,   date = parse("30/6/24")),
                Trip(userId = "G", odometer = 283113, distanceTraveled = 124, price = 1357.3,date = parse("7/7/24")),
                Trip(userId = "A", odometer = 283119, distanceTraveled = 6,   price = 65.7,   date = parse("7/7/24")),
                Trip(userId = "G", odometer = 283270, distanceTraveled = 151, price = 1652.9,date = parse("19/7/24")),
                Trip(userId = "A", odometer = 283279, distanceTraveled = 9,   price = 98.5,   date = parse("20/7/24")),
                Trip(userId = "G", odometer = 283387, distanceTraveled = 108, price = 1182.2,date = parse("26/7/24")),
                Trip(userId = "A", odometer = 283404, distanceTraveled = 17,  price = 186.1,  date = parse("26/7/24")),
                Trip(userId = "G", odometer = 283646, distanceTraveled = 242, price = 2649.0,date = parse("10/8/24")),
                Trip(userId = "A", odometer = 283670, distanceTraveled = 24,  price = 262.7,  date = parse("11/8/24")),
                Trip(userId = "G", odometer = 283793, distanceTraveled = 123, price = 1346.4,date = parse("27/8/24")),
                Trip(userId = "A", odometer = 283797, distanceTraveled = 4,   price = 43.8,   date = parse("27/8/24")),
                Trip(userId = "G", odometer = 283815, distanceTraveled = 18,  price = 197.0,  date = parse("28/8/24")),
                Trip(userId = "A", odometer = 283818, distanceTraveled = 3,   price = 32.8,   date = parse("28/8/24")),
                Trip(userId = "G", odometer = 283926, distanceTraveled = 108, price = 1182.2,date = parse("2/9/24")),
                Trip(userId = "A", odometer = 283932, distanceTraveled = 6,   price = 65.7,   date = parse("2/9/24")),
                Trip(userId = "G", odometer = 283995, distanceTraveled = 63,  price = 689.6,  date = parse("14/9/24")),
                Trip(userId = "A", odometer = 284010, distanceTraveled = 15,  price = 164.2,  date = parse("14/9/24")),
                Trip(userId = "G", odometer = 284632, distanceTraveled = 622, price = 6808.5,date = parse("20/10/24")),
                Trip(userId = "A", odometer = 284649, distanceTraveled = 17,  price = 186.1,  date = parse("20/10/24")),
                Trip(userId = "G", odometer = 284737, distanceTraveled = 88,  price = 963.3,  date = parse("27/10/24")),
                Trip(userId = "A", odometer = 284749, distanceTraveled = 12,  price = 131.4,  date = parse("27/10/24")),
                Trip(userId = "G", odometer = 284822, distanceTraveled = 73,  price = 799.1,  date = parse("7/11/24")),
                Trip(userId = "A", odometer = 284835, distanceTraveled = 13,  price = 142.3,  date = parse("7/11/24")),
                Trip(userId = "G", odometer = 285049, distanceTraveled = 214, price = 2342.5,date = parse("21/11/24")),
                Trip(userId = "A", odometer = 285060, distanceTraveled = 11,  price = 120.4,  date = parse("22/11/24")),
                Trip(userId = "G", odometer = 285695, distanceTraveled = 635, price = 6950.8,date = parse("5/1/25")),
                Trip(userId = "A", odometer = 285709, distanceTraveled = 14,  price = 153.2,  date = parse("5/1/25")),
                Trip(userId = "G", odometer = 286035, distanceTraveled = 326, price = 3568.4,date = parse("8/2/25")),
                Trip(userId = "A", odometer = 286037, distanceTraveled = 2,   price = 21.9,   date = parse("8/2/25")),
                Trip(userId = "G", odometer = 286050, distanceTraveled = 13,  price = 142.3,  date = parse("9/2/25")),
                Trip(userId = "A", odometer = 286315, distanceTraveled = 265, price = 2900.7,date = parse("9/2/25")),
                Trip(userId = "G", odometer = 286342, distanceTraveled = 27,  price = 295.5,  date = parse("24/3/25")),
                Trip(userId = "A", odometer = 286345, distanceTraveled = 3,   price = 32.8,   date = parse("24/3/25")),
                Trip(userId = "G", odometer = 286441, distanceTraveled = 96,  price = 1050.8,date = parse("5/4/25")),
                Trip(userId = "A", odometer = 286447, distanceTraveled = 6,   price = 65.7,   date = parse("7/4/25")),
                Trip(userId = "G", odometer = 286487, distanceTraveled = 40,  price = 437.8,  date = parse("7/4/25")),
                Trip(userId = "A", odometer = 286490, distanceTraveled = 3,   price = 32.8,   date = parse("7/4/25")),
                Trip(userId = "G", odometer = 286540, distanceTraveled = 50,  price = 547.3,  date = parse("12/4/25")),
                Trip(userId = "A", odometer = 286557, distanceTraveled = 17,  price = 186.1,  date = parse("12/4/25")),
                Trip(userId = "A", odometer = 286625, distanceTraveled = 68,  price = 744.3,  date = parse("1/5/25")),
                Trip(userId = "G", odometer = 286715, distanceTraveled = 90,  price = 985.1,  date = parse("7/5/25")),
                Trip(userId = "A", odometer = 286718, distanceTraveled = 3,   price = 32.8,   date = parse("7/5/25"))
            )
        }
    }
}
