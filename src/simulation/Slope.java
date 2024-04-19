package simulation;

public class Slope {

    float current_value;
    float start, end;
    float dist, inc;
    float step_counter;
    float num_of_steps;

    Slope(float start, float end, float num_of_steps) {
        this.start = start;
        this.end = end;
        this.num_of_steps = num_of_steps;
        step_counter = 0;

        dist = end - start;
        inc = dist / num_of_steps;
        current_value = start;
    }

    boolean advanceSlope() {
        if (step_counter < num_of_steps) {
            step_counter++;
            current_value = start + (inc * step_counter);
            return true;
        }
        return false;
    }

    void createSlope(float start, float end, float num_of_steps) {
        this.start = start;
        this.end = end;
        this.num_of_steps = num_of_steps;
        step_counter = 0;

        dist = end - start;
        inc = dist / num_of_steps;
        current_value = start;

    }

}
