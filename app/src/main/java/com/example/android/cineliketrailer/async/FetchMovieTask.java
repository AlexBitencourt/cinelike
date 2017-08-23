package com.example.android.cineliketrailer.async;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.cineliketrailer.BuildConfig;
import com.example.android.cineliketrailer.model.MovieDetails;
import com.example.android.cineliketrailer.processor.MoviesProcessor;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dhiegoabrantes on 25/05/17.
 */

public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<MovieDetails>> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private AsyncTaskDelegator delegator;

    public FetchMovieTask(AsyncTaskDelegator delegator) {
        this.delegator = delegator;
    }


    @Override
    protected ArrayList<MovieDetails> doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        String sort = params[0];
        switch (sort) {
            case "0":
                sort = "popular";
                break;
            case "1":
                sort = "top_rated";
                break;
        }

        // Estes dois precisam ser declarados fora do try / catch para que eles possam ser
        // fechados no bloco finally.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null; // Conterá a resposta crua do JSON como uma string.

        try {
            // Constrói o URL para a consulta TheMovieDb
            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(sort)
                    .appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_MOVIE_MAP_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Cria a solicitação para TheMovieDb e abre a conexão
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Lê o fluxo de entrada em um String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nada para fazer.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Como é JSON, adicionar uma nova linha não é necessário (não afetará a análise)
                // Mas torna a depuração um * lote * mais fácil se você imprimir o preenchido
                // buffer para depuração.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Se córrego estava vazio, nenhum ponto em analisar.
                return null;
            }
            movieJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Movie string: " + movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // Se o código não conseguir obter os dados meteorológicos, não há nenhum ponto
            // para tentar analisar.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return MoviesProcessor.getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieDetails> result) {
        this.delegator.processFinish(result);
    }
}
