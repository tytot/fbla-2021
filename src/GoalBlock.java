import java.awt.Color;

public class GoalBlock extends MapBlock{
    @Override
    public Color getColor() {
        return Color.green;
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
