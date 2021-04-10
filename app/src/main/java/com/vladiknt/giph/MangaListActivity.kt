package com.vladiknt.giph

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MangaListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manga_list)
    }

    fun mangaButton(view: View?) {
        val manga = Intent(this, MangaActivity::class.java)
        when (view!!.id) {
            R.id.my_daughter_became_a_pornstar -> manga.putExtra("name", "My Daughter Became a Pornstar")
            R.id.guide_on_how_to_completely_defeat_boys -> manga.putExtra("name", "Guide on How to Completely Defeat Boys")
            R.id.intolerable_classmate -> manga.putExtra("name", "Intolerable Classmate")
            R.id.otonari_ntr -> manga.putExtra("name", "Otonari NTR")
            R.id.sword_art_online -> manga.putExtra("name", "Sword Art Online")
            R.id.my_very_jealous_wife -> manga.putExtra("name", "My Very Jealous Wife")
            R.id.musuko_ni_nando_mo_kudokarete_konmake_shita_haha -> manga.putExtra("name", "Musuko ni Nando mo Kudokarete Konmake Shita Haha")
            R.id.misaki_full_color -> manga.putExtra("name", "Misaki Full Color")
            R.id.torokase_orgasm -> manga.putExtra("name", "Torokase Orgasm")
            R.id.my_magical_supplement_upon_this_wonderful_wizard -> manga.putExtra("name", "My Magical Supplement upon this Wonderful Wizard")
        }
        startActivity(manga)
    }
}