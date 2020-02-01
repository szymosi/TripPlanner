<template>
<div style="padding: 50px">
    <v-row>
        <v-date-picker v-model="dates" range style="width: 300px; height:400px; " color="#000000"></v-date-picker>
        <div style="width: calc(100% - 300px); padding-left: 50px">
            <v-text-field solo clearable label="Trip name" v-model="name"
                :rules="nameRules" counter="100"></v-text-field>
            <v-textarea solo clearable label="Trip descryption" v-model="descryption"
                :rules="descryptionRules" counter="5000"></v-textarea>

            <h5 v-if="dates[0]&&dates[1]">Start date: {{reverseDate[0]}} End date: {{reverseDate[1]}}</h5>
            <br/>
            <v-btn @click="createTrip" color="#548561" x-large block>Create trip</v-btn>
            <v-btn @click="cancel" color="#548561" x-large block>Cancel</v-btn>
        </div>
    </v-row>
</div>
</template>


<script>
import axios from 'axios';
export default {
    data: function(){
        return{
        name:'',
        descryption:'',
        dates:[],

        message: '',

        nameRules: [v => v.length >=10 || "Name must be minimum 10 characters long",
                    v => v.length <=100 || "Name must be maximum 100 characters long"],
        descryptionRules: [v => v.length <=5000 || "Name must be maximum 5000 characters long"]
        };
    },
    computed:{
        reverseDate: function(){
            if(this.dates[0]>this.dates[1])
                return [this.dates[1],this.dates[0]]
            else 
                return this.dates
        }
    },
    methods:{
        cancel: function(){
            this.$store.commit('setCreatingTrip', false);
        },
        createTrip: function(){
            if(this.name.length<10 ||
            this.name.length>100 ||
            this.descryption.length>5000 ||
            !this.reverseDate[0] && !this.reverseDate[1]){
                this.$toasted.show('Enter valid data', { 
                theme: "outline", 
                position: "top-right", 
                duration : 1000
            })
            return
            }
            return axios.put(this.$url+'/Trip/Create',{
                name: this.name,
                descryption: this.descryption,
                startDate: this.reverseDate[0],
                endDate: this.reverseDate[1]
                
                /*headers:{
                    'Authorization': 'Bearer:'+this.$store.getters.token
                }*/
            }).then(response => {
                this.$toasted.show('Trip '+response.data.name+' created', { 
                theme: "outline", 
                position: "top-right", 
                duration : 1000
                })
                
                this.$store.commit('setTrip', response.data);
                this.$store.commit('setCreatingTrip', false);
                window.location.reload()
                this.$router.push({path:'trip',query: {trip: response.data.id}})
            }).catch(error=>{
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