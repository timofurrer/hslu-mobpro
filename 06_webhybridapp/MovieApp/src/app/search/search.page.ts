import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { NavExtraDataService } from '../nav-extra-data.service';
import { Movie } from '../movie';

@Component({
  selector: 'app-search',
  templateUrl: './search.page.html',
  styleUrls: ['./search.page.scss'],
})
export class SearchPage implements OnInit {

  searchValue: string = "";

  constructor(private http: HttpClient, private router: Router, private navExtras: NavExtraDataService) {
  }

  ngOnInit() {
  }

  search() {
    let movie: Movie;

    this.http.get('http://www.omdbapi.com/?apikey=38ca3104&plot=short&r=json&t=' + this.searchValue).subscribe(response => {
        movie = <Movie>response;
        if(movie.Response != "True") {
            console.log("Error: " + movie.Error);
        }
        else {
            console.log(movie);
            this.navExtras.setExtras(movie)
            this.router.navigate(['/details']);
        }
    });
  }

}
