package p3;

import model.Pair;

public enum AntMovement {
	// Final
	TURN_RIGHT(0),
	TURN_LEFT(0),
	MOVE(0),
	
	// Functions
	ISFOOD(2),
	PROGN2(2),
	PROGN3(3);
	

	public int num_args;
	
	private AntMovement(int args) {
		this.num_args = args;
	}
	
	static AntMovement random_movement() {
		int rnd = (int)(Math.random() * (final_movement.length + node_movement.length));
		if(rnd < final_movement.length) {
			return final_movement[rnd];
		}
		return node_movement[rnd - final_movement.length];
	};
	
	static AntMovement random_node() {
		return node_movement[(int)(Math.random() * node_movement.length)];
	};
	
	static AntMovement random_final() {
		return final_movement[(int)(Math.random() * final_movement.length)];
	};
	
	static AntMovement[] final_movement = {TURN_RIGHT, TURN_LEFT, MOVE};
	static AntMovement[] node_movement = {ISFOOD, PROGN2, PROGN3};
}
