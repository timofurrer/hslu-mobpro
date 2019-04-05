import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';


@Component({
  selector: 'app-search',
  templateUrl: './search.page.html',
  styleUrls: ['./search.page.scss'],
})
export class SearchPage implements OnInit {

  constructor(private http: HttpClient, private router: Router) {
  }

  ngOnInit() {
  }

  search() {
    let movie: Movie;

    this.http.get('http://www.omdbapi.com/?apikey=38ca3104&plot=short&r=json&t=Up').subscribe(response => {
        movie = <Movie>response;
        if(movie.Response != "True") {
            console.log("Error: " + movie.Error);
        }
        else {
            console.log(movie);
            this.router.navigate(['/details', {"movie": movie}]);
        }
    });
  }

}
