

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;


/**
 * Servlet implementation class CurrencyConvertor
 */
@WebServlet("/CurrencyConvertor")
public class CurrencyConvertor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CurrencyConvertor() {
        super();
        // TODO Auto-generated constructor stub
    }


	private static final String Api = "http://api.fixer.io/";

	public static double convert(String convertFrom, String convertTo) {

		if ((convertFrom != null && !convertFrom.isEmpty())
				&& (convertTo != null && !convertTo.isEmpty())) {

			Conversion response = getResponse(Api+"/latest?base="+convertFrom);
			
			if(response != null) {
				
				String rate = response.getRates().get(convertTo);
				
				
				double conversionRate = Double.valueOf((rate != null)?rate:"0.0");
				
				return conversionRate;
			}
			
		}

		return 0.0;
	}


	private static Conversion getResponse(String strUrl) {

		Conversion response = null;
		
		Gson gson = new Gson();
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(strUrl);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			InputStream stream = connection.getInputStream();

			int data = stream.read();

			while (data != -1) {

				sb.append((char) data);

				data = stream.read();
				System.out.println(data);
			}

			stream.close();
        
			
      	response = gson.fromJson(sb.toString(), Conversion.class);

		} catch (Exception e) {
			e.printStackTrace();
			
		}

		return response;
	}

 	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out=response.getWriter();
		String convertFrom=request.getParameter("convert_form");
		String convertTo=request.getParameter("convert_to");
		String amt=request.getParameter("amount");
		double amount=Double.parseDouble(amt);
		response.setContentType("text/html;charset=UTF-8");
		 
        double coversionRate = convert(convertFrom, convertTo);
       
		out.println(amount +" "+ convertFrom+" is equal to "+(coversionRate * amount)+" "+convertTo+" today.");
		

	}

}
class Conversion {

	private String base;
	private String date;

	private Map<String, String> rates = new TreeMap<String, String>();

	public Map<String, String> getRates() {
		return rates;
	}

	public void setRates(Map<String, String> rates) {
		this.rates = rates;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
