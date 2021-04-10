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
            R.id.a_book_where_miku_has_it_her_way -> manga.putExtra("name", "A Book Where Miku Has It Her Way")
            R.id.amayakashi_mate_pia -> manga.putExtra("name", "Amayakashi Mate Pia")
            R.id.azulan_anime_ero_mousou_hon -> manga.putExtra("name", "AzuLan Anime Ero Mousou Hon")
            R.id.bunshin_shite_hamakaze_to_sanketsu_ecchi -> manga.putExtra("name", "Bunshin Shite Hamakaze to Sanketsu Ecchi")
            R.id.introverted_beauty_gets_raped_over_and_over_by_her_homeroom_teacher -> manga.putExtra("name", "Introverted Beauty Gets Raped Over and Over by Her Homeroom Teacher")
            R.id.introverted_beauty_gets_raped_over_and_over_by_her_homeroom_teacher_2 -> manga.putExtra("name", "Introverted Beauty Gets Raped Over and Over by Her Homeroom Teacher 2")
            R.id.kimi_no_namida_no_riyuu_o_ore_wa_mada_shiranai -> manga.putExtra("name", "Kimi no Namida no Riyuu o Ore wa Mada Shiranai")
        }
        startActivity(manga)
    }
}