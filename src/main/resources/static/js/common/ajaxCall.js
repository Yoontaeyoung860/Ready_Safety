'use strict';

const request = {

   get(url) {
      return fetch(url);  //fetch(url,{method:'GET'});
   },

   post(url,payload) {
      return fetch(url, {
         method: 'POST',
         headers:{'content-Type' : 'application/json'},
         body : JSON.stringify(payload)
      })
   },

   patch(url,payload) {
      return fetch(url, {
         method: 'PATCH',
         headers:{'content-Type' : 'application/json'},
         body : JSON.stringify(payload)
      })
   },

   delete(url) {
      return fetch(url, {method:'DELETE'});
   }
}