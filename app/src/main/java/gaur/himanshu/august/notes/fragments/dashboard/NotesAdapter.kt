package gaur.himanshu.august.notes.fragments.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.notes.databinding.NotesViewholderBinding
import gaur.himanshu.august.notes.room.Notes

class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.MyViewHolder>() {


    var list = mutableListOf<Notes>()

    fun setContentList(list: MutableList<Notes>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeItem(note:Notes){
        this.list.remove(note)
       notifyDataSetChanged()
    }

    var handleClick: ((notes: Notes) -> Unit)? = null
    var handleLongClick: ((notes: Notes) -> Unit)? = null

    inner class MyViewHolder(val notesViewholderBinding: NotesViewholderBinding) :
        RecyclerView.ViewHolder(notesViewholderBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.MyViewHolder {
        val binding =
            NotesViewholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesAdapter.MyViewHolder, position: Int) {
        holder.notesViewholderBinding.notes = this.list[position]
        holder.notesViewholderBinding.root.setOnClickListener {
            handleClick?.let {
                it(list[position])
            }
        }

        holder.notesViewholderBinding.root.setOnLongClickListener {
            handleLongClick?.let {
                it(list[position])
            }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return this.list.size
    }


    fun setOnItemClickListener(listener: (note: Notes) -> Unit) {
        handleClick = listener
    }

    fun setOnLongItemClickListener(listener: (note: Notes) -> Unit) {
        handleLongClick = listener
    }

}