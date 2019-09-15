package com.cyrillrx.rpg.models

/**
 * @author Cyril Leroux
 *         Created on 15/09/2019.
 */
class Campaign (
    val game: Game,
    val players: Array<Player>,
    val sessions: Array<Session>)