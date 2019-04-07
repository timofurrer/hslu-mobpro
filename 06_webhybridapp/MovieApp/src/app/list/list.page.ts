import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

import { NavExtraDataService } from '../nav-extra-data.service';
import { Movie } from '../movie';

@Component({
  selector: 'app-list',
  templateUrl: 'list.page.html',
  styleUrls: ['list.page.scss']
})
export class ListPage implements OnInit {
  public movies: Array<Movie> = [];
  private favoriteMovies = [
    'batman',
    'django',
    'grit',
    'pulp',
    'Wolverine'
  ];
  constructor(private http: HttpClient, private router: Router, private navExtras: NavExtraDataService) {
    for (let fav of this.favoriteMovies) {
      this.http.get("../../assets/movies/" + fav + ".json").subscribe(data => {
        let movie: Movie = <Movie>data;
        this.movies.push(movie);
      });
    }
  }

  ngOnInit() {
  }

  selectedMovie(movie) {
    this.navExtras.setExtras(movie)
    this.router.navigate(['/details']);
  }
}
