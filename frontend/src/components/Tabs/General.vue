<template>
<div style="height: calc(100vh - 150px); background-color:#a7b8a3">
    <v-row style="width: 100%; padding: 50px">
        <div v-if=isOrganizer style="width: 50%">
            <v-text-field clearable label="Trip name" v-model=name
                :rules="nameRules" counter="100"></v-text-field>
            <v-textarea clearable label="Trip descryption" v-model=descryption
                :rules="descryptionRules" counter="5000"></v-textarea>
            <v-row>
                <v-text-field clearable label="Start date" v-model=startDate style="width: 130px; padding: 15px"></v-text-field>
                <v-text-field clearable label="End date" v-model=endDate style="width: 130px; padding: 15px"></v-text-field>
            </v-row>
            <v-btn @click="updateTrip" color="#548561" x-large block>Update trip</v-btn>
        </div>
        <div v-else style="width: 50%">
            <v-text-field label="Trip name" v-model=name readonly></v-text-field>
            <v-textarea label="Trip descryption" v-model=descryption readonly></v-textarea>
            <v-row>
                <v-text-field label="Start date" v-model=startDate readonly></v-text-field>
                <v-text-field label="End date" v-model=endDate readonly></v-text-field>
            </v-row>
        </div>

        
        <div style="width: 50%; padding-left: 50px">
            <h4 style="text-align: left">Participants:</h4>
            <b-list-group>
                <list v-for="user in participants" v-bind:key="user">
                    <b-list-group-item v-if=isOrganizer id="ListElement" button @click="setParticipant(user)">{{user.username}}</b-list-group-item>
                    <b-list-group-item v-else id="ListElement">{{user.username}}</b-list-group-item>
                </list>
            </b-list-group>
            <v-pagination
                id="pagination"
                @input="getParticipants(participantsPage-1)"
                v-if="participantsPages>1"
                v-model="participantsPage"
                color=#000000
                :circle=true
                :length=this.participantsPages
            ></v-pagination>
            <v-row v-if=isOrganizer style="padding: 15px">
                <v-text-field clearable label="Username" v-model=username style="padding-right: 10px; width: 40%"></v-text-field>
                <v-btn @click="addParticipant" color="#548561" large style="width: 160px">Add participant</v-btn>

            </v-row>
            <v-btn v-if="this.participant!=null" id="deleteUserbtn" retain-focus-on-click color="#548561" x-large block>Remove {{participant.username}} from trip</v-btn>
                <b-popover v-if="this.participant!=null" target="deleteUserbtn" triggers="focus" placement="bottom">
                    <h6>Are you sure you want to remove user {{this.participant.username}} from trip</h6>
                    <b-button @click="removeParticipant" block style="background-color: #687864;">Yes</b-button>
                </b-popover>
        </div>
    </v-row>
    <v-btn id="deleteTripbtn" retain-focus-on-click style="width: calc(100% - 70px); position: absolute; bottom: 0px; left: 35px;" v-if=isOrganizer color="#548561" x-large>Delete trip</v-btn>
        <b-popover target="deleteTripbtn" triggers="focus" placement="top">
            <h6>Are you sure you want to delete trip {{this.name}}</h6>
            <b-button @click="deleteTrip" block style="background-color: #687864;">Yes</b-button>
        </b-popover>
</div>
</template>


<script>
import axios from 'axios';
export default {
    data: function(){
        return{
        name:this.$store.getters.trip.name,
        descryption:this.$store.getters.trip.descryption,
        startDate:this.$store.getters.trip.startDate,
        endDate:this.$store.getters.trip.endDate,

        participants: [],
        participantsPage: 0,
        participantsPages: 0,

        participant: null,

        username: "",

        message: '',

        nameRules: [v => v.length >=10 || "Name must be minimum 10 characters long",
                    v => v.length <=100 || "Name must be maximum 100 characters long"],
        descryptionRules: [v => v.length <=5000 || "Name must be maximum 5000 characters long"]
        };
    },
    computed:{
        isOrganizer: function(){
            return this.$store.getters.user.id==this.$store.getters.trip.organizer
        }
    },
    mounted: function(){
        this.getParticipants(0);
    },
    methods:{
        setParticipant: function(user){
            this.participant=user
        },
        updateTrip: function(){
            if(this.name.length<10 ||
            this.name.length>100 ||
            this.length>5000){
                this.$toasted.show('Enter valid data', { 
                theme: "outline", 
                position: "top-right", 
                duration : 1000
            })
            return
            }
            axios.defaults.headers.common['Authorization']='Bearer:'+this.$store.getters.token,
            axios.post(this.$url+'/Trip/Update'+'?tripId='+this.$store.getters.trip.id,{
                name: this.name,
                descryption: this.descryption,
                startDate: this.startDate,
                endDate: this.endDate,

                
            }).then(response => {
                this.$toasted.show('Trip '+response.data.name+' updated', { 
                theme: "outline", 
                position: "top-right", 
                duration : 1000
                })
                
                this.$store.commit('setTrip', response.data);
                //window.location.reload()
                this.$router.push({path:'trip',query: {trip: response.data.id}})
            }).catch(error=>{
                this.showError(error);
            })
        },
        deleteTrip: function(){
            return axios.delete(this.$url+'/Trip/Delete',{
            headers:{
                Authorization: 'Bearer:'+this.$store.getters.token
            },
            params: {
                tripId: this.$store.getters.trip.id,
            }
            })
            .then(response => {                
                this.$store.commit('setCreatingTrip', false);
                this.$store.commit('setTrip', null);
                this.$router.push({path:''})
                //window.location.reload()

                this.$toasted.show(response.name+" deleted", { 
                theme: "outline", 
                position: "top-right", 
                duration : 1000
                })
            })
            .catch(error=>{
                this.showError(error)
            })                    
        },
        getParticipants: function(page){
            return axios.get(this.$url+'/Trip/Participants',{
            headers:{
                Authorization: 'Bearer:'+this.$store.getters.token
            },
            params: {
                page: page,
                size: '8',
                tripId: this.$store.getters.trip.id,
            }
            })
            .then(response => {
                this.participantsPages=response.data.totalPages;
                this.participants=response.data.content;
                this.message=response;
            })
            .catch(error=>{
                this.showError(error)
            })            
        },
        addParticipant: function(){
            axios.defaults.headers.common['Authorization']='Bearer:'+this.$store.getters.token,
            axios.put(this.$url+'/Trip/AddParticipant'+
                '?participantUsername='+this.username+
                '&tripId='+this.$store.getters.trip.id,
            )
            .then(response => {
            this.$toasted.show(response.message, { 
            theme: "outline", 
            position: "top-right", 
            duration : 1000
            })
            })
            .catch(error=>{
                this.showError(error)
            })      
            //window.location.reload()
        },
        removeParticipant: function(){
            return axios.delete(this.$url+'/Trip/RemoveParticipant',{
            headers:{
                Authorization: 'Bearer:'+this.$store.getters.token
            },
            params: {
                participantId: this.participant.id,
                tripId: this.$store.getters.trip.id,
            }
            })
            .then(response => {
            this.$toasted.show("Participant "+response.data.username+" removed", { 
            theme: "outline", 
            position: "top-right", 
            duration : 1000
            })
            })
            .catch(error=>{
                this.showError(error)
            })
            //window.location.reload()             
        },
        showError: function(error){
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

  #ListElement:active{
    background-color: #000000;
  }
</style>
