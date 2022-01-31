package Computer;
import java.awt.Point;

public class TreeNode {
	private Point toolIdx;
	private Point toIdx;
	private double evaluation = 50000;

	public TreeNode(boolean maximizingPlayer)
	{
		if(maximizingPlayer)
			this.evaluation = - Double.MAX_VALUE;
		else
			this.evaluation = Double.MAX_VALUE;
	}
	
	public double getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(double evaluation) {
		this.evaluation = evaluation;
	}
	public Point getToIdx() {
		return toIdx;
	}
	public void setToIdx(Point toIdx) {
		this.toIdx = toIdx;
	}
	public Point getToolIdx() {
		return toolIdx;
	}
	public void setToolIdx(Point toolIdx) {
		this.toolIdx = toolIdx;
	}

}
