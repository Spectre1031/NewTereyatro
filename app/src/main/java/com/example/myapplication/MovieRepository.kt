package com.example.myapplication

import androidx.annotation.DrawableRes
import com.example.myapplication.R
import com.example.myapplication.Movie

data class Movie(
    val id: String,
    val imageName: String,
    val year: Int,
    val title: String,
    val synopsis: String
)

object MovieRepository {

    val movies = listOf(

        Movie("2020001", "cominghome", 2020,
            "Coming Home (2020)", "A family drama that follows the journey of a man returning to his hometown after years of absence, confronting past relationships and unresolved issues."
        ),
        Movie("2020003", "isapangbahaghari", 2020,
            "Isa Pang Bahaghari (2020)", "A family drama that explores the complexities of relationships within a family, focusing on themes of love, acceptance, and reconciliation."
        ),
        Movie("2020004", "magikland", 2020,
            "Magikland (2020)", "A fantasy-adventure film that follows a group of children who embark on a magical journey to save their beloved theme park from impending doom."
        ),
        Movie("2020005", "mangkepweng", 2020,
            "Mang Kepweng: Ang Lihim ng Bandanang Itim (2020)", "A comedy film that follows the adventures of a man who discovers a magical black bandana that grants him extraordinary powers, leading to humorous situations."
        ),
        Movie("2020006", "pakboystakusa", 2020,
            "Pakboys Takusa (2020)", "A comedy film that follows the misadventures of a group of friends who find themselves in unexpected situations, leading to laughter and chaos."
        ),
        Movie("2020007", "suarez", 2020,
            "Suarez: The Healing Priest (2020)", "A biographical drama that tells the story of a priest known for his healing powers, exploring his life, faith, and the challenges he faced."
        ),
        Movie("2020008", "tagpuan", 2020,
            "Tagpuan (2020)", "A romance film that explores the complexities of love and relationships, focusing on the intertwining lives of its characters."
        ),
        Movie("2020009", "theboyforetoldbythestars", 2020,
            "The Boy Foretold By The Stars (2020)", "A romantic comedy that follows the journey of a young man who discovers love in unexpected circumstances, challenging societal norms."
        ),
        Movie("2020010", "fangirl", 2020,
            "Fan Girl (2020)", "A drama that delves into the life of a young girl who becomes obsessed with a famous actor, leading to unexpected events that challenge her perceptions of fame and reality."),


        // 2021

        Movie("2021001", "nelia", 2021,
            "Nelia (2021)", "A suspense thriller about a nurse who becomes entangled in a series of mysterious events that threaten her life and sanity."
        ),
        Movie("2021002", "theexorsis", 2021,
            "The Exorsis (2021)", "A horror-comedy about two sisters who confront supernatural forces in their home, leading to a series of comedic and terrifying events."
        ),
        Movie("2021003", "ahardday", 2021,
            "A Hard Day (2021)", "A gripping action film about a police officer who finds himself entangled in a series of unfortunate events after a tragic accident. "
        ),
        Movie("2021004", "bignight", 2021,
            "Big Night (2021)", "A dark comedy that follows the life of a gay man who dreams of a grand celebration, only to face unexpected challenges that test his resilience.  "
        ),
        Movie("2021005", "loveatfirststream", 2021,
            "Love At First Stream (2021)", "A romantic comedy about four young individuals who navigate the complexities of love and relationships in the digital age.  "
        ),
        Movie("2021006", "whethertheweatherisfine", 2021,
            "Kun Maupay Man It Panahon (2021)", "A drama set in the aftermath of Typhoon Haiyan, focusing on the lives of a mother and son as they navigate the challenges of survival and rebuilding."
        ),
        Movie("2021007", "huwagkanglalabas", 2021,
            "Huwag Kang Lalabas (2021)", "An anthology horror film that delves into the lives of individuals who experience terrifying events when they defy warnings to stay indoors. "
        ),
        Movie("2021008", "hulingulansatagaraw", 2021,
            "Huling Ulan Sa Tag-Araw (2021)", "A romantic comedy that explores the complexities of relationships and the impact of past experiences on present love.  "
        ),

        // 2022

        Movie("2022003", "mamasapano", 2022,
            "Mamasapano (2022)", "A gripping action-drama that recounts the controversial Mamasapano clash, shedding light on the events and the individuals involved."
        ),
        Movie("2022004", "myfathermyself", 2022,
            "My Father, Myself (2022)", "A compelling drama that delves into the complexities of father-son relationships, exploring themes of identity, acceptance, and personal growth."
        ),
        Movie("2022005", "labyuwithanaccent", 2022,
            "Labyu With an Accent (2022)", "A romantic comedy that follows the journey of a couple navigating the challenges of a cross-cultural relationship, highlighting themes of love and understanding."
        ),
        Movie("2022006", "nanahimikanggabi", 2022,
            "Nanahimik ang Gabi (2022)", "A suspenseful horror-thriller that delves into the eerie events surrounding a family's encounter with supernatural forces during a stormy night."
        ),
        Movie("2022007", "myteacher", 2022,
            "My Teacher (2022)", "A heartwarming drama that explores the transformative power of education through the story of a dedicated teacher and her students."
        ),
        Movie("2022008", "deleter", 2022,
            "Deleter (2022)", "A psychological techno-horror thriller that delves into the life of a social media content moderator who becomes entangled in a series of disturbing events after a traumatic incident.  "
        ),
        Movie("2022009", "familymatters", 2022,
            "Family Matters (2022)", "A heartfelt family drama that explores the complexities of relationships within a family, focusing on themes of love, acceptance, and reconciliation. "
        ),

        // 2023

        Movie("2023002", "kampon", 2023,
            "Kampon (2023)", "A horror thriller that delves into the supernatural occurrences surrounding a family, exploring themes of fear and survival."
        ),
        Movie("2023003", "penduko", 2023,
            "Penduko (2023)", "A superhero film that reimagines the classic Filipino character Pedro Penduko, focusing on his journey to embrace his destiny."
        ),
        Movie("2023004", "rewind", 2023,
            "Rewind (2023)", "A romantic drama that explores the complexities of love and relationships, highlighting the challenges couples face."
        ),
        Movie("2023005", "beckyandbadette", 2023,
            "Becky And Badette (2023)", "A comedy that follows the misadventures of two women as they navigate the ups and downs of their friendship and personal lives."
        ),
        Movie("2023006", "brokenheartstrip", 2023,
            "Broken Hearts Trip (2023)", "A drama-comedy that explores the journey of two individuals who embark on a trip to heal from their past heartbreaks."
        ),
        Movie("2023009", "mallari", 2023,
            "Mallari (2023)", "A horror-thriller that delves into the life of a man who becomes entangled in a series of mysterious events after a tragic incident."
        ),
        Movie("20230010", "familyoftwo", 2023,
            "Family Of Two (2023)", "A heartfelt drama about a mother and son navigating the challenges of their relationship and the complexities of family dynamics.  "
        ),

        // 2024

        Movie("2024002", "myfutureyou", 2024,
            "My Future You (2024)", "A romantic comedy that follows the journey of two individuals who embark on a trip to heal from their past heartbreaks."
        ),
        Movie("2024003", "isanghimala", 2024,
            "Isang Himala (2024)", "A musical drama that explores the transformative power of faith through the story of a dedicated teacher and her students."
        ),
        Movie("2024004", "thekingdom", 2024,
            "The Kingdom (2024)", "An epic action-drama that chronicles the rise and fall of a powerful kingdom, exploring themes of power, betrayal, and legacy."
        ),
        Movie("2024005", "topakk", 2024,
            "Topakk (2024)", "An action-thriller that follows the journey of a man who becomes entangled in a series of mysterious events after a tragic incident."
        ),
        Movie("2024006", "uninvited", 2024,
            "Uninvited (2024)", "A psychological thriller that follows the story of a woman who becomes entangled in a series of disturbing events after a traumatic incident."
        ),
        Movie("2024007", "filler_image", 2024,
            "And The Breadwinner Is... (2024)", "A film that focuses on a breadwinner and her family, which serves as a tribute to the unsung heroes who carry the weight of their loved ones' dreams on their shoulders."
        )
    )
}