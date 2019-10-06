/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eci.mathappwebservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import spark.*;
import static spark.Spark.*;

/**
 * SparkWebApp Aplicación en Spark que será desplegada en AWS como un servicio EC2.
 * Calcula el cuadrado de un numero ingresado en index.
 * @author Amalia
 */

public class SparkWebApp {

    private static String url = "https://mx6jgln02a.execute-api.us-east-1.amazonaws.com/Beta";

    public static void main(String[] args) throws MalformedURLException {

        port(getPort());

        get("/hello", (request, response) -> "Hello Mundo");

        get("/square", (request, response) -> calculateSquare(request));

        get("/inicio", (request, response) -> generateHtml());
    }

    /**
     * generateHtml Génera el código html que será enviado al navegador como
     * respuesta de su petición
     *
     * @param meanS Media calculada de los números dados
     * @param standardDeviationS Desviación Estándar calculada de los números
     * dados
     * @param numerosSplit Números dados por el navegador
     * @return String con código del html
     */
    private static String generateHtml() {
        String respuesta = "<!DOCTYPE html>\n"
                + "<html lang=\"en\">\n"
                + "    <head>\n"
                + "        <title>Spark App</title>\n"
                + "        <meta charset=\"utf-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n"
                + "        <meta name=\"description\" content=\"\">\n"
                + "        <meta name=\"author\" content=\"\">\n"
                + "\n"
                + "        <!-- Bootstrap core CSS -->\n"
                + "        <link href=\"bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\n"
                + "        <!-- Custom fonts for this template -->\n"
                + "        <link href=\"https://fonts.googleapis.com/css?family=Catamaran:100,200,300,400,500,600,700,800,900\" rel=\"stylesheet\">\n"
                + "        <!-- Custom styles for this template -->\n"
                + "        <link href=\"css/one-page-wonder.min.css\" rel=\"stylesheet\">\n"
                + "\n"
                + "        <script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\" integrity=\"sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo\" crossorigin=\"anonymous\"></script>\n"
                + "        <script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\" integrity=\"sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1\" crossorigin=\"anonymous\"></script>\n"
                + "        <script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\" integrity=\"sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM\" crossorigin=\"anonymous\"></script>\n"
                + "    </head>\n"
                + "\n"
                + "    <body style=\"background-image: url('fondo.jpg'); background-position: center center; background-repeat: no-repeat; background-attachment: fixed; background-size: cover;\">\n"
                + "            <!-- Navigation -->\n"
                + "            <nav class=\"navbar navbar-expand-lg navbar-dark navbar-custom fixed-top\">\n"
                + "                <div class=\"container\">\n"
                + "                    <a class=\"navbar-brand\" >AREP-SparkWebApp AWS</a>\n"
                + "                    <a class=\"navbar-brand\" >Amalia Alfonso</a>\n"
                + "                </div>\n"
                + "            </nav>\n"
                + "            <header class=\"masthead text-center text-white\">\n"
                + "                <div class=\"container\" style=\"text-align:center;\">\n"
                + "                     <form action=\"/square\">\n"
                + "                        <div class=\"form-group\" style=\"text-align:center;\">\n"
                + "                        <h5 class=\"masthead-heading mb-0\" style=\"color: indigo; font-size: 3.2em\">Cuadrado</h5>\n"
                + "                        <br>\n"
                + "                        <label style=\"color: black; size: 18px;\">Numero</label>\n"
                + "                        <input class='form-control' type=\"text\" placeholder=\"Ejemplo: 5\"  width=\"50%\" name=\"numero\"> \n"
                + "                         <br> "
                + "                         <br> "
                + "                        <button type=\"submit\" class=\"btn btn-primary btn-xl\" >Calcular</button>  \n"
                + "                        </div>\n"
                + "                    </form>"
                + "                </div>\n"
                + "            </header>\n"
                + "    </body>\n"
                + "</html>";
        return respuesta;
    }

    /**
     * getPort Retorna el puerto por defecto de heroku
     *
     * @return
     */
    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e.on localhost)
    }

    /**
     * calculateSquare Retorna el cuadrado del numero ingresado en el index. Lee
     * el parámetro del request y hace uso del servicio en AWS que usa Amazon
     * Gateway y lambda, el cual calcula y retorna el cuadrado de un número.
     *
     * @param request Petición enviada por cliente (navegador).
     * @return Cuadrado del número ingresado
     * @throws MalformedURLException excepción al leer del buffer.
     * @throws IOException excepcion de entrada y salida.
     */
    private static String calculateSquare(Request request) throws MalformedURLException, IOException {
        String rta = "";
        int parametro = Integer.parseInt(request.queryParams("numero"));
        System.out.println(parametro);
        System.out.println(request);
        URL urlParam = new URL(url + "?value=" + parametro);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlParam.openStream()));
            String dataLine = "";
            while ((dataLine = reader.readLine()) != null) {
                rta += dataLine;
                //System.out.println(urlData);   
            }
        } catch (MalformedURLException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }

        return rta;
    }

}
