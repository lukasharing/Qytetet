package tracks.singlePlayer.advanced.p1;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types;
import tools.ElapsedCpuTimer;

public class Agent extends AbstractPlayer {

    public tracks.singlePlayer.advanced.p1.LukasPlayer player;

    public Agent(StateObservation so, ElapsedCpuTimer elapsedTimer) {
        //Create the player.
        player = new tracks.singlePlayer.advanced.p1.LukasPlayer(so, this);
    }

    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {
        return player.run(stateObs, elapsedTimer);
    }

}
