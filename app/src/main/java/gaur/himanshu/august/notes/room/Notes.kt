package gaur.himanshu.august.notes.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
class Notes(
    val title: String,
    val description: String,
    val email: String = "",
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) : Parcelable