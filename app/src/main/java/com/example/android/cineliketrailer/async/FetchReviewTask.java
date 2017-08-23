package com.example.android.cineliketrailer.async;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.cineliketrailer.BuildConfig;
import com.example.android.cineliketrailer.model.MovieReview;
import com.example.android.cineliketrailer.processor.ReviewsProcessor;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alexbitencourt on 10/07/17.
 */
public class FetchReviewTask extends AsyncTask<String, Void, ArrayList<MovieReview>> {

    private final String LOG_TAG = FetchReviewTask.class.getSimpleName();

    private AsyncTaskDelegatorReview delegatorReview;

    public FetchReviewTask(AsyncTaskDelegatorReview delegator) {
        this.delegatorReview = delegator;
    }


    @Override
    protected ArrayList<MovieReview> doInBackground(String... params) {

        // Se não houver um código postal, não há nada para procurar. Verifique o tamanho dos params.
        if (params.length == 0) {
            return null;
        }

        // Estes dois precisam ser declarados fora do try / catch para que eles possam ser
        // fechados no bloco finally.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewJsonStr = null; // Conterá a resposta crua do JSON como uma string.

        try {
            // Constrói o URL para a consulta TheMovieDb

            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String MOVIE_ID = params[0];
            final String MDB_API_REVIEWS = "reviews";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(MOVIE_ID)
                    .appendPath(MDB_API_REVIEWS)
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
            reviewJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Movie string: " + reviewJsonStr);
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
            return ReviewsProcessor.getReviewDataFromJson(reviewJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieReview> result) {
        this.delegatorReview.processFinishReview(result);

    }

}
