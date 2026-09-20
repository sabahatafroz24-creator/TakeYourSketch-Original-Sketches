package com.example.model

data class Member(
    val id: String,
    val name: String,
    val role: String,
    val bio: String = ""
)

val TysCollectiveMembers = listOf(
    Member(
        id = "1",
        name = "Trinabh Gupta",
        role = "Founder, Director",
        bio = "Curates the sketchbook archive and oversees single-original verification and studio curation."
    ),
    Member(
        id = "2",
        name = "Afnaan Wadood Siddiqui",
        role = "Co founder and Artist",
        bio = "Draftsperson specializing in pen & wash, street angles, and rapid gestural ink sketches."
    ),
    Member(
        id = "3",
        name = "Saesha Chillarega",
        role = "Manager and Mandala artist",
        bio = "Resident geometric artist creating intricate, zero-reproduction mandala drawings with fine archival nibs."
    ),
    Member(
        id = "4",
        name = "Garvit Tiwari",
        role = "Assistant Manager and Artist",
        bio = "Charcoal specialist focused on fast live gesture captures and evocative chiaroscuro lighting."
    ),
    Member(
        id = "5",
        name = "Karthik Mishra",
        role = "Business Partner (CEO of KGGT)",
        bio = "Oversees artist relations, rigid packaging flat-mail operations, and gallery partnerships."
    )
)
