<template>
    <div>
      <v-btn id="createButton" block tile depressed height="47px" @click="createTrip">Create Trip</v-btn>
      <b-nav-text v-if="organizingPages>0" style="color:#eef7e9; font-size:25px">Organizing:</b-nav-text>
      <b-list-group>
         <list v-for="trip in organizingTrips" v-bind:key="trip">
          <b-list-group-item id="ListElement" button @click="setChoosenTrip(trip)">{{trip.name}}</b-list-group-item>
        </list>
      </b-list-group>
      <v-pagination
        id="pagination"
        @input="getOrganizingTrips(organizingPage-1)"
        v-if="organizingPages>1"
        v-model="organizingPage"
        color=#000000
        :circle=true
        :length=this.organizingPages
      ></v-pagination>
      <br>
      <b-nav-text v-if="participatingPages>0" style="color:#eef7e9; font-size:25px">Participating:</b-nav-text>
      <b-list-group>
        <list v-for="trip in participatingTrips" v-bind:key="trip">
          <b-list-group-item id="ListElement" button @click="setChoosenTrip(trip)">{{trip.name}}</b-list-group-item>
        </list>
      </b-list-group>
      <v-pagination
        id="pagination"
        @input="getParticipatingTrips(organizingPage-1)"
        v-if="participatingPages>1"
        v-model="participatingPage"
        color=#000000
        :circle=true
        :length=this.participatingPages
      ></v-pagination>
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
    createTrip: function(){
      this.$router.push({path:'trip', query: {trip: undefined}})
      this.$store.commit('setCreatingTrip', true);
    },
    setChoosenTrip: function(trip){
      this.$store.commit('setCreatingTrip', false);
      this.$store.commit('setTrip', trip);
      this.$router.push({path:'trip',query: {trip: trip.id}})
      window.location.reload()
    },
    getOrganizingTrips: function(page){
      return axios.get(this.$url+'/Trip/Organizer',{
      headers:{
        Authorization: 'Bearer:'+this.$store.getters.token
      },
      params: {
        page: page,
        size: '8'
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
      return axios.get(this.$url+'/Trip/Participant',{
      headers:{
        Authorization: 'Bearer:'+this.$store.getters.token
      },
      params: {
        page: page,
        size: '8'
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
    font-size:16px;
    color: #eef7e9;
    border-radius:0px;
    border: 0px
  }  

  #ListElement:hover{
    background-color: #3e7a2f;
  }
</style>
