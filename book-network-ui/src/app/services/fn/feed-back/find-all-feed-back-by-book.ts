/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseFeedBackResponse } from '../../models/page-response-feed-back-response';

export interface FindAllFeedBackByBook$Params {
  book_id: number;
  page?: number;
  size?: number;
}

export function findAllFeedBackByBook(http: HttpClient, rootUrl: string, params: FindAllFeedBackByBook$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseFeedBackResponse>> {
  const rb = new RequestBuilder(rootUrl, findAllFeedBackByBook.PATH, 'get');
  if (params) {
    rb.path('book_id', params.book_id, {});
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'blob', accept: '*/*', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseFeedBackResponse>;
    })
  );
}

findAllFeedBackByBook.PATH = '/feedbacks/book/{book_id}';
