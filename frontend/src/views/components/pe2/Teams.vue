<template>
  <div>
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="12" vs-sm="12" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <h2 class="float-left" style="color: cornflowerblue">Teams</h2>
          </div>
          <div class="float-right mb-1">
            <vs-button v-show="teams.length === 0" @click="generateTeams()" color="primary" type="filled">Generate Teams
            </vs-button>
            <vs-button v-show="teams.length !== 0" @click="download()" class="mr-1" icon="get_app" color="primary" type="filled">Download CSV
            </vs-button>

            <vs-button v-show="teams.length !== 0" @click="deleteAllTeams()" class="ml-1" color="danger" icon="delete" type="filled">
              Delete All Teams
            </vs-button>
          </div>
          <div class="table-responsive">
            <table class="table v-middle border">
              <thead>
              <tr class="">
                <th class="border-top-0" style="color: cornflowerblue">ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Name</th>
                <th class="border-top-0" style="color: cornflowerblue">Project ID</th>
                <th class="border-top-0" style="color: cornflowerblue">Project Name</th>
                <th class="border-top-0" style="color: cornflowerblue">Number of Students</th>
                <th class="border-top-0" style="color: cornflowerblue">Actions</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="team in teams" :key="team.id">
                <td>{{ team.id }}</td>
                <td>{{ team.name }}</td>
                <td>{{ team.projectId }}</td>
                <td>{{ team.projectName }}</td>
                <td>{{ team.numberOfStudents }}</td>
                <td>
                  <div>
                    <vs-button @click="currentTeamID = team.id; deleteForm = true" icon="delete" class="m-1" color="danger"
                               type="filled">
                      Delete
                    </vs-button>
                    <vs-button @click='$router.push({name:"Teams Edit", params: {team: team.id}});'
                               icon="edit" class="m-1" color="warning" type="filled">
                      Edit
                    </vs-button>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </vs-card>
      </vs-col>
      <vs-prompt
          title="Deletion"
          color="red"
          @close='notify("Closed","Deletion was cancelled.","warning")'
          @cancel='notify("Closed","Deletion was cancelled.","warning")'
          @accept="deleteTeam(currentTeamID)"
          :is-valid="true"
          :active.sync="deleteForm"
      >
        <div class="con-exemple-prompt">
          Are you sure you want to delete the Team with the ID  <br>
          <h6>{{ currentTeamID }}</h6>
        </div>
      </vs-prompt>
    </vs-row>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "Teams",
  data: () => {
    return {
      teams: [],
      deleteForm: false,
      currentTeamID : 0,
      dataForCSV: []
    }
  },

  created: async function () {
    //   await this.deleteAllTeams()
    await this.fetchAllTeams()


  },

  methods: {

    download: async function () {
      //  for (let i=0;i<this.teams.length;i++){
      await axios.get("http://localhost:8080/api/v1/csv-downloads/teams/").then((response) =>  {
        var csv = response.data
        var hiddenElement = document.createElement('a');
        hiddenElement.href = 'data:text/csv;charset=utf-8,' + encodeURIComponent(csv);
        hiddenElement.target = '_blank';
        hiddenElement.download =  'teams.csv';
        hiddenElement.click();
        this.notify("Success","CSV file was successfully created","success")
      })
    },


    deleteAllTeams: async function () {
      this.teams = []
      await axios.delete(`http://localhost:8080/api/v1/team-allocation`);
    },
    fetchAllTeams: async function () {
       await axios.get(`http://localhost:8080/api/v1/teams`)
          .then((response) => {
            this.teams = response.data
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Teams Database Error", error.message, "danger")
            } else {
              this.notify("Teams Database Error", "Connection to Database Error", "danger")
            }
          })
    },
    generateTeams: async function () {
   //   this.teams = []
      await axios.post(`http://localhost:8080/api/v1/team-allocation`, {})
          .then((response) => {
            this.teams = response.data
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Employees Database Error", error.message, "danger")
            } else {
              this.notify("Employees Database Error", "Connection to Database Error", "danger")
            }
          })
    },

    /** Shows notification with title, message and selected color*/
    notify: function (title, message, color) {
      this.$vs.notify({
        title: title,
        text: message,
        color: color, type: "gradient",
      })
    },


    deleteTeam: async function (id) {
      // send DELETE request to API to delete a specific cat by ID
      this.teams = this.teams.filter((team) => team.id !== id);
      await axios.delete(`http://localhost:8080/api/v1/teams/` + id.toString())
      this.notify("Confirmation", "Team has been  successfully deleted", "danger")
    },

  },

}

</script>

<style scoped>

</style>
