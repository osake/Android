package com.eftimoff.fonts.net;

import com.eftimoff.fonts.models.Fonts;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Query;

public interface WebFontsService {

	@Headers("Cache-Control: public, max-age=640000, s-maxage=640000 , max-stale=2419200")
	@GET("/webfonts/v1/webfonts?fields=items(category%2Cfamily%2Cfiles%2ClastModified%2Csubsets%2Cvariants%2Cversion)")
	Fonts listFonts(@Query("sort") String sort, @Query("key") String key);

}