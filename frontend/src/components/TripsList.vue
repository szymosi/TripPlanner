<template>
    <div>
        <b-nav-text v-if="organizingPages>0" style="color:#eef7e9; font-size:25px">Organizing:</b-nav-text>
        <b-list-group>
          <list v-for="trip in organizingTrips" v-bind:key="trip">
            <b-list-group-item id="ListElement" button @click="setChoosenTrip(trip)">{{trip.name}}</b-list-group-item>
          </list>
        </b-list-group>
        <b-pagination id="Pagination" v-if="organizingPages>0" v-model="organizingPage" @change="getOrganizingTrips(organizingPage-1)" :total-rows=this.organizingPages :per-page=1 hide-goto-end-buttons align="right" size="sm"></b-pagination>
        <br>

        <b-nav-text v-if="participatingPages>0" style="color:#eef7e9; font-size:25px">Participating:</b-nav-text>
        <b-list-group>
          <list v-for="trip in participatingTrips" v-bind:key="trip">
            <b-list-group-item id="ListElement" button>{{trip.name}}</b-list-group-item>
          </list>
        </b-list-group>
        <b-pagination v-if="participatingPages>0" v-model="participatingPage" @change="getParticipatingTrips(participatingPage-1)" :total-rows=this.participatingPages :per-page=1 hide-goto-end-buttons align="right" size="sm"></b-pagination>
       
    </div>
</template>

<script>
import axios from 'axios';
export default {
      data: function(){
        return{
          organizingTrips: [],
          participatingTrips: [],

          organizingPage: 0,
          participatingPage: 0,

          organizingPages: 0,
          participatingPages: 0,
          
          message: '',
      };
  },

  mounted: function() {
    this.getOrganizingTrips(0)
    this.getParticipatingTrips(0)
  },
  
  methods:{
    setChoosenTrip: function(trip){
      this.$store.commit('setTrip', trip);
      this.$router.push({path:'trip',query: {trip: trip.id}})
    },
    getOrganizingTrips: function(page){
      return axios.get('http://localhost:8181/Trip/Organizer',{
      headers:{
        Authorization: 'Bearer:'+this.$store.getters.token
      },
      params: {
        page: page,
        size: '1'
      }
    })
    .then(response => {
      this.organizingPages=response.data.totalPages;
      this.organizingTrips=response.data.content;
      })
    .catch(error=>{
      if(error.response){
        this.message=error.response.data.message
      }
      else if(error.request){
        this.message=error.request
      }
      else {
        this.message=error
      }
      this.$toasted.show(this.message, { 
      theme: "outline", 
      position: "top-right", 
      duration : 1000
      })
    })
    },

    getParticipatingTrips: function(page){
      return axios.get('http://localhost:8181/Trip/Participant',{
      headers:{
        Authorization: 'Bearer:'+this.$store.getters.token
      },
      params: {
        page: page,
        size: '10'
      }
    })
    .then(response => {
      this.participatingPages=response.data.totalPages;
      this.participatingTrips=response.data.content;
      })
    .catch(error=>{
      if(error.response){
        this.message=error.response.data.message
      }
      else if(error.request){
        this.message=error.request
      }
      else {
        this.message=error
      }
      this.$toasted.show(this.message, { 
      theme: "outline", 
      position: "top-right", 
      duration : 1000
      })
    })
    }
  }
}
  
</script>

<style scoped>
  list{list-style:none}

  #ListElement{
    background-color: #687864;
    font-size:20px;
    color: #eef7e9;
    border-radius:0px;
    border: 0px
  }  

  #ListElement:hover{
    background-color: #3e7a2f;
  }

  #ListElement:active{
    background-color: #000000;
  }
</style>