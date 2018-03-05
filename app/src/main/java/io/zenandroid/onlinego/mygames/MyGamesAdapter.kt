package io.zenandroid.onlinego.mygames

import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.zenandroid.onlinego.R
import io.zenandroid.onlinego.gamelogic.RulesManager
import io.zenandroid.onlinego.ogs.OGSServiceImpl
import io.zenandroid.onlinego.reusable.GamesAdapter
import io.zenandroid.onlinego.utils.egfToRank
import io.zenandroid.onlinego.utils.formatRank
import io.zenandroid.onlinego.views.BoardView

/**
 * Created by alex on 03/03/2018.
 */
class MyGamesAdapter : GamesAdapter<MyGamesAdapter.ViewHolder>() {
    private val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
    private val normalTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val game = gameList[position]
        holder.boardView.boardSize = game.width
        gameDataMap[game.id]?.let { gameData ->
            val pos = RulesManager.replay(gameData, computeTerritory = false)

            holder.boardView.position = pos
            val opponent =
                    when (OGSServiceImpl.instance.uiConfig?.user?.id) {
                        gameData.players?.black?.id -> gameData.players?.white
                        gameData.players?.white?.id -> gameData.players?.black
                        else -> null
                    }
            holder.opponentName.text = opponent?.username
            holder.opponentRank.text = formatRank(egfToRank(gameData.players?.black?.egf))
//            holder.blackName.text = gameData.players?.black?.username
//            holder.blackRank.text = formatRank(egfToRank(gameData.players?.black?.egf))
//            holder.whiteName.text = gameData.players?.white?.username
//            holder.whiteRank.text = formatRank(egfToRank(gameData.players?.white?.egf))

//            if(pos.getStoneAt(pos.lastMove) != StoneType.BLACK) {
//                holder.blackName.typeface = boldTypeface
//                holder.whiteName.typeface = normalTypeface
//            } else {
//                holder.blackName.typeface = normalTypeface
//                holder.whiteName.typeface = boldTypeface
//            }
        }
        holder.itemView.setOnClickListener {
            clicksSubject.onNext(game)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game_card, parent, false)
        return ViewHolder(
                view,
                view.findViewById(R.id.board),
                view.findViewById(R.id.opponent_name),
                view.findViewById(R.id.opponent_rank)
        )
    }

    class ViewHolder(
            itemView: View,
            val boardView: BoardView,
            val opponentName: TextView,
            val opponentRank: TextView
    ) : RecyclerView.ViewHolder(itemView)
}