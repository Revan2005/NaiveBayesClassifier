import java.util.ArrayList;


public class Model {
	ArrayList<Obiekt> daneUczace;
	Atrybut atrybutKlasowy;
	int liczbaKlas;
	int liczbaAtrybutow;
	int[] licznosciKlas;
	double[] apriori;
	boolean czyZdyskretyzowane;
	//int lS; //z lS nie korzystam
	
	//z deugiego atrybutu nie korzystam!!!
	public Model(ArrayList<Obiekt> dU, boolean czyZdyskretyzowane){
		//lS = liczbaSasiadow;
		this.czyZdyskretyzowane = czyZdyskretyzowane;
		daneUczace = dU;
		atrybutKlasowy = Atrybuty.getAtrybutKlasowy();
		liczbaKlas = atrybutKlasowy.getLiczbaMozliwychWartosciLubPrzedzialow();
		liczbaAtrybutow = Atrybuty.getLiczbaAtrybutowZKlasa()-1;
		apriori = new double[liczbaKlas];
		licznosciKlas = new int[liczbaKlas];
		String rozwazanaKlasa;
		Obiekt o;
		for(int i=0; i<liczbaKlas; i++){
			rozwazanaKlasa = atrybutKlasowy.dziedzina.get(i);
			for(int j=0; j<daneUczace.size(); j++){
				o = daneUczace.get(j);
				if(o.getEtykietaKlasy().equals(rozwazanaKlasa)){
					licznosciKlas[i]++;
				}
			}	
		}
		
		for(int i=0; i<liczbaKlas; i++){
			apriori[i] = (double)licznosciKlas[i]/daneUczace.size();
		}
		
		
	}
	
	public String getClass(Obiekt o){
		if(czyZdyskretyzowane)
			return getClassAtrybutyZdyskretyzowane(o);
		else
			return getClassAtrybutyNieZdyskretyzowane(o);
	}
	
	public String getClassAtrybutyZdyskretyzowane(Obiekt o){
		//int liczbaSasiadow = lS;
		//ustal sasiadow o
		//liczbaSasiadow jako parametr modelu lS
		//sasiedztwo wzgledem poszczegolnych atrybutow licznos...[i][j] = liczba obiektow klasy j
		//wsrod liczbaSasiadow obiektow najblizszych o pod wzgledem wartosci atrybutu i
		//ArrayList<Obiekt> najblizsiSasiedzi;
		int[][] licznosciKlasObiektowZTymSamymAtrybutem = new int[liczbaAtrybutow][liczbaKlas];
		Atrybut rozwazanyAtrybut;
		Przedzial przedzialOTestowego, przedzialOUczacego;
		String wartoscEnumOTestowego, wartoscEnumOUczacego;
		Obiekt oUczacy;
		int indexKlasy;
		for(int i=0; i<liczbaAtrybutow; i++){
			rozwazanyAtrybut = Atrybuty.get(i);
			if(rozwazanyAtrybut.czyNumeryczny()){
				przedzialOTestowego = rozwazanyAtrybut.getPrzedzialDoKtoregoWpada(o.wartosciAtrybutowNumerycznych[i]);
				for(int j=0; j<daneUczace.size(); j++){
					oUczacy = daneUczace.get(j);
					przedzialOUczacego = rozwazanyAtrybut.getPrzedzialDoKtoregoWpada(oUczacy.wartosciAtrybutowNumerycznych[i]);
					if( przedzialOTestowego.equals(przedzialOUczacego) ){
						//jaka to klasa odp. 
						indexKlasy = Atrybuty.getIndeksKlasy(oUczacy.getEtykietaKlasy());
						licznosciKlasObiektowZTymSamymAtrybutem[i][indexKlasy]++;
					}
				}
			} else {
				wartoscEnumOTestowego = o.wartosciAtrybutowEnum[i];
				for(int j=0; j<daneUczace.size(); j++){
					oUczacy = daneUczace.get(j);
					wartoscEnumOUczacego = oUczacy.wartosciAtrybutowEnum[i];
					if( wartoscEnumOTestowego.equals(wartoscEnumOUczacego) ){
						//jaka to klasa odp. 
						indexKlasy = Atrybuty.getIndeksKlasy(oUczacy.getEtykietaKlasy());
						licznosciKlasObiektowZTymSamymAtrybutem[i][indexKlasy]++;
					}
				}
			}
		}
		
		
		double[] szansa = new double[liczbaKlas];
		for(int i=0; i<liczbaKlas; i++){
			szansa[i] = 1;
			for(int j=0; j<liczbaAtrybutow; j++)
				szansa[i] *= (double)(licznosciKlasObiektowZTymSamymAtrybutem[j][i]+1)/(licznosciKlas[i]+1);
		}
		
		double[] aposteriori = new double[liczbaKlas];
		for(int i=0; i<liczbaKlas; i++)
			aposteriori[i] = apriori[i]*szansa[i];
		
		//znajdz maksymalna ppb interesuje nie indeks klasy
		double aposterioriMax = aposteriori[0];
		int indexMax = 0;
		for(int i=0; i<liczbaKlas; i++){
			if(aposteriori[i]>aposterioriMax){
				aposterioriMax = aposteriori[i];
				indexMax = i;
			}
		}
		
		String decyzja = atrybutKlasowy.getWartosc(indexMax);
			
		return decyzja;
	}
	

	
	public String getClassAtrybutyNieZdyskretyzowane(Obiekt o){
		int liczbaAtrybutow = Atrybuty.getLiczbaAtrybutowZKlasa()-1;
		int[][] licznosciKlasObiektowZTymSamymAtrybutem = new int[liczbaAtrybutow][liczbaKlas];
		Atrybut rozwazanyAtrybut;
		Przedzial przedzialOTestowego, przedzialOUczacego;
		String wartoscEnumOTestowego, wartoscEnumOUczacego;
		Obiekt oUczacy;
		int indexKlasy;
		for(int i=0; i<liczbaAtrybutow; i++){
			rozwazanyAtrybut = Atrybuty.get(i);
			if(rozwazanyAtrybut.czyNumeryczny()){
				;
			} else {
				wartoscEnumOTestowego = o.wartosciAtrybutowEnum[i];
				for(int j=0; j<daneUczace.size(); j++){
					oUczacy = daneUczace.get(j);
					wartoscEnumOUczacego = oUczacy.wartosciAtrybutowEnum[i];
					if( wartoscEnumOTestowego.equals(wartoscEnumOUczacego) ){
						//jaka to klasa odp. 
						indexKlasy = Atrybuty.getIndeksKlasy(oUczacy.getEtykietaKlasy());
						licznosciKlasObiektowZTymSamymAtrybutem[i][indexKlasy]++;
					}
				}
			}
			
	
		}
		
		double[][][] miSigma;
		Obiekt rozwazanyObiekt;
		ArrayList<ArrayList<Obiekt>> daneUczacePodzieloneNaKlasy = new ArrayList<ArrayList<Obiekt>>();
		for(int i=0; i<liczbaKlas; i++)
			daneUczacePodzieloneNaKlasy.add(new ArrayList<Obiekt>());
		for(int i=0; i<daneUczace.size(); i++){
			rozwazanyObiekt = daneUczace.get(i);
			indexKlasy = Atrybuty.getIndeksKlasy(rozwazanyObiekt.getEtykietaKlasy());
			//System.out.println("Index klasy = "+indexKlasy);
			daneUczacePodzieloneNaKlasy.get(indexKlasy).add(rozwazanyObiekt);
		}
		miSigma = new double[liczbaKlas][liczbaAtrybutow][2];
		for(int i=0; i<daneUczacePodzieloneNaKlasy.size(); i++){
			//System.out.println("daneUczacePodzieloneNaKlasy.get("+i+").size() "+daneUczacePodzieloneNaKlasy.get(i).size());
			miSigma[i] = calculateMiSigma(daneUczacePodzieloneNaKlasy.get(i));
			//for(int j=0; j<liczbaAtrybutow; j++)
			//	System.out.println("Atrybut: "+j+":   mi = "+miSigma[i][j][0]+"  sigma = "+miSigma[i][j][1]);
		}
		/*
		double[][] miSigmaCalegoZbioru = calculateMiSigma(daneUczace);
		for(int i=0; i<liczbaAtrybutow; i++)
			System.out.println("Misigma dla calego zbioru Atrybut: "+i+"   mi = "+miSigmaCalegoZbioru[i][0]+"  sigma = "+miSigmaCalegoZbioru[i][1]);
		*/
		double[][] estymator = new double[liczbaKlas][liczbaAtrybutow];
		double wartoscObiektuTestowego;
		for(int i=0; i<liczbaKlas; i++){
			for(int j=0; j<liczbaAtrybutow; j++){
				if(daneUczacePodzieloneNaKlasy.get(i).size() == 0){
					estymator[i][j] = 1;
				} else {
					wartoscObiektuTestowego = o.wartosciAtrybutowNumerycznych[j];
					estymator[i][j] = calculateEstymator(miSigma[i][j], wartoscObiektuTestowego);//calculateEstymator(miSigma[i][j], miSigma[i][j][0]);
				}
			}
		}

		double[] szansa = new double[liczbaKlas];
		for(int i=0; i<liczbaKlas; i++){
			szansa[i] = 1;
			for(int j=0; j<liczbaAtrybutow; j++){
				if(Atrybuty.get(j).czyNumeryczny()){
					szansa[i] *= (double)estymator[i][j];
					//System.out.println("estymator: "+estymator[i][j]);
				} else {
					szansa[i] *= (double)(licznosciKlasObiektowZTymSamymAtrybutem[j][i]+1)/(licznosciKlas[i]+1);
				}
			}
			//System.out.println("klasa "+i+",  szansa = "+szansa[i]);
		}
		
		double[] aposteriori = new double[liczbaKlas];
		for(int i=0; i<liczbaKlas; i++)
			aposteriori[i] = apriori[i]*szansa[i];
		
		//znajdz maksymalna ppb interesuje nie indeks klasy
		double aposterioriMax = aposteriori[0];
		int indexMax = 0;
		for(int i=0; i<liczbaKlas; i++){
			if(aposteriori[i]>aposterioriMax){
				aposterioriMax = aposteriori[i];
				indexMax = i;
			}
		}
		
		String decyzja = atrybutKlasowy.getWartosc(indexMax);
			
		return decyzja;
	}
	
	public double[][] calculateMiSigma(ArrayList<Obiekt> obiektyJednejKlasy){
		double[][] miSigma = new double[liczbaAtrybutow][2];
		double suma, sumaKwadratowOdleglosci, mi=0, sigma=0, sigmaKwadrat;
		Obiekt o;
		for(int i=0; i<liczbaAtrybutow; i++){
			if(Atrybuty.get(i).czyNumeryczny()){
				suma = 0;
				for(int j=0; j<obiektyJednejKlasy.size(); j++){
					o = obiektyJednejKlasy.get(j);
					suma += o.wartosciAtrybutowNumerycznych[i];
				}
				mi = suma/obiektyJednejKlasy.size();
				sumaKwadratowOdleglosci = 0;
				for(int j=0; j<obiektyJednejKlasy.size(); j++){
					o = obiektyJednejKlasy.get(j);
					sumaKwadratowOdleglosci += Math.pow((o.wartosciAtrybutowNumerycznych[i] - mi), 2);
				}
				sigmaKwadrat = sumaKwadratowOdleglosci/(obiektyJednejKlasy.size()-1);
				sigma = Math.sqrt(sigmaKwadrat);
			}
			if(sigma == 0)
				sigma = 0.0000001;
			miSigma[i][0] = mi;
			miSigma[i][1] = sigma;
			//System.out.println("atrybut: "+i+",  mi="+mi+" sigma="+sigma);
		}
		return miSigma;
	}
	
	public double calculateEstymator(double[] miSigma, double x){
		double mi = miSigma[0], sigma = miSigma[1];
		double podstawa = 1/(sigma*Math.sqrt(2));
		double exp = Math.exp(-Math.pow((x-mi), 2)/(2*sigma*sigma));
//ZLE nie dziala dla wine i glass MOJ pomysl, dobrze?? -na koncu dopisalem sigma zeby "znormalizowac rozklad normalny" (przy malym sigma dzwon jest bardzo wysoki) tu tkwi blad!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
		double estymator = podstawa*exp;
		
		//System.out.println("mi="+mi+" sigma="+sigma+" estymator="+estymator);
		return estymator;
	}
	
	/*public double[] getMinMaxValueOfAttr(ArrayList<Obiekt> lista, int indAttr){
		Obiekt o = lista.get(0);
		double min = o.wartosciAtrybutowNumerycznych[indAttr], max = o.wartosciAtrybutowNumerycznych[indAttr];
		for(int i=0; i<lista.size(); i++){
			if(o.wartosciAtrybutowNumerycznych[indAttr] < min)
				min = o.wartosciAtrybutowNumerycznych[indAttr];
			if(o.wartosciAtrybutowNumerycznych[indAttr] > max)
				max = o.wartosciAtrybutowNumerycznych[indAttr];
		}
		double[] minMax = new double[2];
		minMax[0] = min;
		minMax[1] = max;
		return minMax;
	}*/
	
}
