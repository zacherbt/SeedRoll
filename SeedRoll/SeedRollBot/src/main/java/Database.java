import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Database {

    private PriorityQueue<Rollplayer> playingThisWeek;
    private Set<Rollplayer> database;

    public Database() {
        playingThisWeek = new PriorityQueue<Rollplayer>();
        database = new HashSet<Rollplayer>();
    }

    public String selectPlayer() {
        Rollplayer selectedPlayer = playingThisWeek.poll();
        return selectedPlayer.getID();
    }

    public void markParticipation(ReactionPaginationAction players) {
        String userID = "";
        for(User ID : players) {
            userID = ID.getId();
            if (!database.contains(userID)) {
                Rollplayer currPlayer = new Rollplayer(userID, true);
                database.add(currPlayer);
                playingThisWeek.add(currPlayer);
            } else {
                for (Rollplayer currPlayer : database)  {
                    if (currPlayer.getID() == userID) {
                        currPlayer.setParticipation(true);
                        playingThisWeek.add(currPlayer);
                    }
                }
            }
        }
    }

    public void progressTime() {
        for(Rollplayer nonPlayer : database) {
            if (!nonPlayer.playedLastWeek()) {
                nonPlayer.decreasePriority();
            } else {
                nonPlayer.increasePriority();
            }
            nonPlayer.setParticipation(false);
        }
        playingThisWeek.clear();
    }

    public void printDatabase() {
        for (Rollplayer player : database) {
            System.out.println(player.getID() + ", " + player.getPriority());
        }
    }

    public int size() {
        return playingThisWeek.size();
    }

    public boolean noPlayerLeft() {
        return playingThisWeek.isEmpty();
    }
}
