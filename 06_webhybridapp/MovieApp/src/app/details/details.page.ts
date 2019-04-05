import { Component, OnInit } from '@angular/core';

import { NavExtraDataService } from '../nav-extra-data.service.ts';

@Component({
  selector: 'app-details',
  templateUrl: './details.page.html',
  styleUrls: ['./details.page.scss'],
})
export class DetailsPage implements OnInit {

  movie: Movie;

  constructor(private navExtras: NavExtraDataService) {
    this.movie = this.navExtras.getExtras();
  }

  ngOnInit() {
  }

}
