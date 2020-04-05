package tracks.singlePlayer.advanced.p1;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer {

    public tracks.singlePlayer.advanced.p1.LukasPlayer player;

    public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
        //Create the player.
        elapsedTimer.setMaxTimeMillis(900L);
        player = new tracks.singlePlayer.advanced.p1.LukasPlayer(so, this, elapsedTimer);
    }

    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        elapsedTimer.setMaxTimeMillis(30L); // 30 ms = 50 * 0.6
        return player.run(stateObs, elapsedTimer);
    }

}
