package edu.unitn.dii.foursquare;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;
import com.google.code.geocoder.model.LatLngBounds;

public class TNGeoCoder {
	
	final static Geocoder geocoder = new Geocoder();
	
	private final  static ArrayList<String> CENTRI =  new ArrayList<String>(){
		private static final long serialVersionUID = 1L;

	{
		add("ALA"); add("ALBIANO"); add("ALDENO"); add("ALDINO"); add("AMBLAR"); add("ANDRIANO"); add("ANTERIVO"); add("APPIANO SULLA STRADA DEL VINO"); add("ARCO"); add("AVELENGO"); add("AVIO"); add("BADIA"); add("BARBIANO"); add("BEDOLLO"); add("BERSONE"); add("BESENELLO"); add("BEZZECCA"); add("BIENO"); add("BLEGGIO SUPERIORE"); add("BOCENAGO"); add("BOLBENO"); add("BOLZANO"); add("BONDO"); add("BONDONE"); add("BORGO VALSUGANA"); add("BOSENTINO"); add("BRAIES"); add("BRENNERO"); add("BRESIMO"); add("BRESSANONE"); add("BREZ"); add("BRIONE"); add("BRONZOLO"); add("BRUNICO"); add("CADERZONE"); add("CAINES"); add("CALAVINO"); add("CALCERANICA AL LAGO"); add("CALDARO SULLA STRADA DEL VINO"); add("CALDES"); add("CALDONAZZO"); add("CAMPITELLO DI FASSA"); add("CAMPO DI TRENS"); add("CAMPO TURES"); add("CAMPODENNO"); add("CANAZEI"); add("CAPRIANA"); add("CARANO"); add("CARZANO"); add("CASTEL CONDINO"); add("CASTELBELLO-CIARDES"); add("CASTELFONDO"); add("CASTELLO-MOLINA DI FIEMME"); add("CASTELLO TESINO"); add("CASTELNUOVO"); add("CASTELROTTO"); add("CAVALESE"); add("CAVARENO"); add("CAVEDAGO"); add("CAVEDINE"); add("CAVIZZANA"); add("CEMBRA"); add("CENTA SAN NICOLO"); add("CERMES"); add("CHIENES"); add("CHIUSA"); add("CIMONE"); add("CIS"); add("CIVEZZANO"); add("CLES"); add("CLOZ"); add("COMMEZZADURA"); add("CONCEI"); add("CONDINO"); add("CORNEDO ALL'ISARCO"); add("CORTACCIA SULLA STRADA DEL VINO"); add("CORTINA SULLA STRADA DEL VINO"); add("CORVARA IN BADIA"); add("CROVIANA"); add("CUNEVO"); add("CURON VENOSTA"); add("DAIANO"); add("DAMBEL"); add("DAONE"); add("DARE&apos;"); add("DENNO"); add("DIMARO"); add("DOBBIACO"); add("DON"); add("DORSINO"); add("DRENA"); add("DRO"); add("EGNA"); add("FAEDO"); add("FAI DELLA PAGANELLA"); add("FALZES"); add("FAVER"); add("FIAVE&apos;"); add("FIE&apos; ALLO SCILIAR"); add("FIERA DI PRIMIERO"); add("FIEROZZO"); add("FLAVON"); add("FOLGARIA"); add("FONDO"); add("FORNACE"); add("FORTEZZA"); add("FRASSILONGO"); add("FUNES"); add("GAIS"); add("GARGAZZONE"); add("GARNIGA"); add("GIOVO"); add("GLORENZA"); add("GRAUNO"); add("GRIGNO"); add("GRUMES"); add("ISERA"); add("IVANO-FRACENA"); add("LA VALLE"); add("LACES"); add("LAION"); add("LAIVES"); add("LANA"); add("LASA"); add("LASINO"); add("LAUREGNO"); add("LAVARONE"); add("LAVIS"); add("LEVICO TERME"); add("LISIGNAGO"); add("LIVO"); add("LOMASO"); add("LUSERNA"); add("LUSON"); add("MAGRE&apos; SULLA STRADA DEL VINO"); add("MALE&apos;"); add("MALLES VENOSTA"); add("MAREBBE"); add("MARLENGO"); add("MARTELLO"); add("MAZZIN"); add("MELTINA"); add("MERANO"); add("MEZZANA"); add("MEZZANO"); add("MEZZOCORONA"); add("MEZZOLOMBARDO"); add("MOENA"); add("MOLINA DI LEDRO"); add("MOLVENO"); add("MONCLASSICO"); add("MONGUELFO"); add("MONTAGNA"); add("MONTAGNE"); add("MORI"); add("MOSO IN PASSIRIA"); add("NAGO-TORBOLE"); add("NALLES"); add("NANNO"); add("NATURNO"); add("NAVE SAN ROCCO"); add("NAZ-SCIAVES"); add("NOGAREDO"); add("NOMI"); add("NOVA LEVANTE"); add("NOVA PONENTE"); add("NOVALEDO"); add("ORA"); add("ORTISEI"); add("OSPEDALETTO"); add("OSSANA"); add("PADERGNONE"); add("PALU&apos; DEL FERSINA"); add("PANCHIA&apos;"); add("PARCINES"); add("PEIO"); add("PELUGO"); add("PERCA"); add("PIEVE DI BONO"); add("PIEVE DI LEDRO"); add("PINZOLO"); add("PLAUS"); add("POMAROLO"); add("PONTE GARDENA"); add("POSTAL"); add("POZZA DI FASSA"); add("PRASO"); add("PRATO ALLO STELVIO"); add("PREDAZZO"); add("PREDOI"); add("PREORE"); add("PREZZO"); add("PROVES"); add("RABBI"); add("RACINES"); add("RASUN ANTERSELVA"); add("RENON"); add("REVO&apos;"); add("RIFIANO"); add("RIO DI PUSTERIA"); add("RODENGO"); add("ROMALLO"); add("ROMENO"); add("RONCEGNO"); add("RONCHI VALSUGANA"); add("RONZO-CHIENIS"); add("ROVERE&apos; DELLA LUNA"); add("ROVERETO"); add("RUFFRE&apos;"); add("RUMO"); add("SAGRON MIS"); add("SALORNO"); add("SAMONE"); add("SAN CANDIDO"); add("SAN GENESIO ATESINO"); add("SAN LEONARDO IN PASSIRIA"); add("SAN LORENZO DI SEBATO"); add("SAN LORENZO IN BANALE"); add("SAN MARTINO IN BADIA"); add("SAN MARTINO IN PASSIRIA"); add("SAN MICHELE ALL&apos;ADIGE"); add("SAN PANCRAZIO"); add("SANT&apos;ORSOLA TERME"); add("SANTA CRISTINA VALGARDENA"); add("SANZENO"); add("SARENTINO"); add("SARNONICO"); add("SCENA"); add("SCURELLE"); add("SEGONZANO"); add("SELVA DEI MOLINI"); add("SELVA DI VAL GARDENA"); add("SENALE-SAN FELICE"); add("SENALES"); add("SESTO"); add("SFRUZ"); add("SILANDRO"); add("SIROR"); add("SLUDERNO"); add("SMARANO"); add("SOVER"); add("SPORMAGGIORE"); add("SPORMINORE"); add("STELVIO"); add("STORO"); add("STRIGNO"); add("TAIO"); add("TELVE"); add("TELVE DI SOPRA"); add("TENNA"); add("TENNO"); add("TERENTO"); add("TERLAGO"); add("TERLANO"); add("TERRAGNOLO"); add("TERRES"); add("TERZOLAS"); add("TESERO"); add("TESIMO"); add("TIARNO DI SOPRA"); add("TIARNO DI SOTTO"); add("TIRES"); add("TIROLO"); add("TON"); add("TORCEGNO"); add("TRAMBILENO"); add("TRANSACQUA"); add("TRENTO"); add("TRES"); add("TRODENA"); add("TUBRE"); add("TUENNO"); add("ULTIMO"); add("VADENA"); add("VAL DI VIZZE"); add("VALDA"); add("VALDAORA"); add("VALFLORIANA"); add("VALLARSA"); add("VALLE AURINA"); add("VALLE DI CASIES"); add("VANDOIES"); add("VARENA"); add("VARNA"); add("VATTARO"); add("VELTURNO"); add("VERANO"); add("VERMIGLIO"); add("VERVO&apos;"); add("VEZZANO"); add("VIGNOLA-FALESINA"); add("VIGO DI FASSA"); add("VIGO RENDENA"); add("VIGOLO VATTARO"); add("VILLA AGNEDO"); add("VILLA LAGARINA"); add("VILLA RENDENA"); add("VILLABASSA"); add("VILLANDRO"); add("VIPITENO"); add("VOLANO"); add("ZIANO DI FIEMME"); add("ANDALO"); add("BASELGA DI PINE"); add("BLEGGIO INFERIORE"); add("BREGUZZO"); add("BRENTONICO"); add("CAGNO"); add("CALLIANO"); add("CANAL SAN BOVO"); add("CARISOLO"); add("CIMEGO"); add("CINTE TESINO"); add("COREDO"); add("GIUSTINO"); add("IMER"); add("LAGUNDO"); add("LARDARO"); add("LONA-LASES"); add("MALOSCO"); add("MASSIMENO"); add("PELLIZZANO"); add("PERGINE VALSUGANA"); add("PIEVE TESINO"); add("RAGOLI"); add("RIVA DEL GARDA"); add("RONCONE"); add("RONZONE"); add("SORAGA"); add("SPERA"); add("SPIAZZO"); add("STENICO"); add("STREMBO"); add("TASSULLO"); add("TERMENO SULLA STRADA DEL VINO"); add("TIONE DI TRENTO"); add("TONADICO"); add("ZAMBANA"); add("ZUCLO");
	}};
	
	public static void main(String args[]){
		
		Iterator<String> itr = CENTRI.iterator();
		String center="";
		ArrayList<String> points= PointsOfInterest.GEO_POINTS;
		Iterator<String> pitr = points.iterator();
		int i=0;
		while(pitr.hasNext()){
		  try {
				Thread.sleep(500);
				String coordinates = pitr.next();
				int index = coordinates.indexOf(",");
				double lat = Double.valueOf(coordinates.substring(0, index));
				double lng = Double.valueOf(coordinates.substring(index+1));
				LatLng ll= new LatLng(new BigDecimal(lat),new BigDecimal(lng));
				GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(ll).setLanguage("it").getGeocoderRequest();
				GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
				System.out.println(""+i+". "+geocoderResponse.getResults().get(0).getFormattedAddress());
				System.out.println(""+geocoderResponse.getResults().get(0).getGeometry().getBounds());
				i++;
		  }catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }
		}
		while(itr.hasNext()){
			
		  try {
			Thread.sleep(1000);
			center=itr.next();
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(center+",  Trentino Alto Adige , Italy").setLanguage("it").getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			//System.out.println(geocoderResponse.getStatus());
			
			if(geocoderResponse.getStatus()==GeocoderStatus.OK){
				
				ArrayList<GeocoderResult> results = (ArrayList<GeocoderResult>) geocoderResponse.getResults();
				//System.out.println("\n============================================================ \n");
				for( int j=0; j< results.size(); j++){
					ArrayList<GeocoderAddressComponent> addressComponents= (ArrayList<GeocoderAddressComponent>) results.get(j).getAddressComponents();
					System.out.println(center+": "+results.get(j).getGeometry().getLocation().getLat()+", "+results.get(j).getGeometry().getLocation().getLng());
					/*System.out.println("Formated Address: "+results.get(i).getFormattedAddress());	
					System.out.println("LatLng: "+results.get(i).getGeometry().getLocation().getLat()+", "+results.get(i).getGeometry().getLocation().getLng());
					System.out.println("Location Type: "+results.get(i).getGeometry().getLocationType());
					System.out.println("ViewPort: "+results.get(i).getGeometry().getViewport().getNortheast()+" | "+results.get(i).getGeometry().getViewport().getSouthwest());
					System.out.println("Longitude: "+results.get(i).getGeometry().getLocation().getLng());
					LatLngBounds bounds= results.get(i).getGeometry().getBounds();
					System.out.println("Bound Northwest: "+bounds.getNortheast());
					System.out.println("Bound SouthEast: "+bounds.getSouthwest());
					for( int j=0; j< addressComponents.size(); j++){				
						System.out.println("Short Name: "+addressComponents.get(j).getShortName());
						System.out.println("Long Name: "+addressComponents.get(j).getLongName());
					}*/
				}
			}
		  }catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }
		}
	}
	

}
