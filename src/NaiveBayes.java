import java.util.ArrayList;


public abstract class NaiveBayes {

	//z drugiego argumentu nie korzystam
	public static Model generujModel(ArrayList<Obiekt> daneUczace, boolean czyZdyskretyzowane){
		return new Model(daneUczace, czyZdyskretyzowane);
	}
}
