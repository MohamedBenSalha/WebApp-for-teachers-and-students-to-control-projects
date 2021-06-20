<template>
  <div>
    <vs-row vs-justify="center">
      <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="12"
              vs-sm="6" vs-xs="12"
              code-toggler>
        <vs-card class="cardx">
          <div slot="header">
            <div class="float-right mb-1">
              <vs-button color="success" v-bind:disabled="!validUpdate()" @click="updateTeam()" type="relief">Update
                Team
              </vs-button>
            </div>

            <h5 class="mb-2"> Update Team Name</h5>
            <vs-input type="text" v-model="newNameProject" :placeholder="team.name" :value="team.name"></vs-input>
            <p class="mt-2" v-show="allTeamsNames.filter(name => name !== team.name).includes(newNameProject)"
               style="color:red;">This name has already been attributed to another team ! </p>
          </div>
        </vs-card>
      </vs-col>


      <vs-row vs-justify="center">
        <vs-col type="flex" vs-justify="center" vs-align="center" :vs-lg="12"
                vs-sm="6" vs-xs="12"
                code-toggler>
          <vs-card class="cardx">
            <div slot="header">
              <h2 class="float-left" style="color: cornflowerblue">Students List </h2>
              <p v-show="team.students.length===0" style="color:red; margin-left: 250px">There must be at least one
                student in the list ! </p>
              <div class="float-right mb-1">
                <vs-button @click="addForm = true" color="primary" icon="add" type="filled">Add New
                  Student
                </vs-button>
              </div>
            </div>
            <div class="table-responsive">
              <table class="table v-middle border">

                <thead>
                <tr class="">
                  <th class="border-top-0" style="color: cornflowerblue">Name</th>
                  <th class="border-top-0" style="color: cornflowerblue">Prename</th>
                  <th class="border-top-0" style="color: cornflowerblue">Email</th>
                  <th class="border-top-0" style="color: cornflowerblue">ILIAS Name</th>
                  <th class="border-top-0" style="color: cornflowerblue">Actions</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(student,index) in team.students" :key="student.index">
                  <td> {{ student.name }}</td>
                  <td> {{ student.prename }}</td>
                  <td> {{ student.email }}</td>
                  <td> {{ student.iliasName }}</td>
                  <td>
                    <div>
                      <vs-button @click="deleteForm = true; currentStudent = student" icon="delete" class="m-1"
                                 color="danger"
                                 type="filled">
                        Delete
                      </vs-button>
                      <vs-button @click="getStudentData(index);editForm=true" icon="edit" class="m-1" color="warning"
                                 type="filled">
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
            title="Add New Student"
            color="success"
            @cancel='resetAllValues();notify("Closed","Add was closed successfully","warning")'
            @accept="addStudent"
            @close='resetAllValues();notify("Closed","Add was closed successfully","warning")'
            :is-valid="validStudent()"
            :active.sync="addForm"
        >
          <div class="con-exemple-prompt">
            <vs-input label-placeholder="Name" v-model="name"/>
            <small class="form-text text-muted mb-4">The name must not be empty</small>
            <vs-input label-placeholder="Prename" v-model="prename"/>
            <small class="form-text text-muted mb-4">The name must not be empty.</small>
            <vs-input label-placeholder="Email" type="mail" v-model="email"/>
            <small class="form-text text-muted mb-4">The Email must and end with
              <i>@stud.uni-stuttgart.de</i></small>
            <vs-input label-placeholder="ILIAS Name" v-model="iliasName"/>
            <small class="form-text text-muted mb-4">The first character must be a Capital letter.</small>
            <vs-alert
                :active="!validStudent()"
                color="danger"
                icon="new_releases"
            >

              Your input does not match the needed one
              or this student exists in another registration
            </vs-alert>

          </div>
        </vs-prompt>

        <vs-prompt
            title="Edit Student"
            color="success"
            @cancel='resetAllValues();notify("Closed","Edit was closed successfully","warning")'
            @accept="saveChanges()"
            @close='resetAllValues();notify("Closed","Edit was closed successfully","warning")'
            :is-valid="validStudentEdit()"
            :active.sync="editForm"
        >
          <div class="con-exemple-prompt">
            <vs-input label="Name" :placeholder="nameEdit" v-model="nameEdit"/>
            <small class="form-text text-muted mb-4">The name must not be empty</small>
            <vs-input label="Prename" :placeholder="prenameEdit" v-model="prenameEdit"/>
            <small class="form-text text-muted mb-4">The name must not be empty.</small>
            <vs-input label="Email" type="mail" :placeholder="emailEdit" v-model="emailEdit"/>
            <small class="form-text text-muted mb-4">The Email must and end with
              <i>@stud.uni-stuttgart.de</i></small>
            <vs-input label="ILIAS Name" :placeholder="iliasEdit" v-model="iliasEdit"/>
            <small class="form-text text-muted mb-4">The first character must be a Capital letter.</small>
            <vs-alert
                :active="!validStudentEdit()"
                color="danger"
                icon="new_releases"
            >

              Your input does not match the needed one
              or this student exists in another registration
            </vs-alert>

          </div>
        </vs-prompt>

        <vs-prompt
            title="Deletion"
            color="red"
            @close='notify("Closed","Deletion was cancelled.","warning")'
            @cancel='notify("Closed","Deletion was cancelled.","warning")'
            @accept="deleteStudent(currentStudent)"
            :is-valid="true"
            :active.sync="deleteForm"
        >
          <div class="con-exemple-prompt">
            Are you sure you want to delete <br>
            <h6>{{ currentStudent.prename }}</h6>
          </div>
        </vs-prompt>


      </vs-row>


    </vs-row>


  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: "TeamsEdit",
  data() {
    return {
      teamID: 0,
      newNameProject: "",
      allTeamsNames: [],
      registrationsStudents: [],
      team: [],
      currentStudent: {},

      name: '',
      prename: '',
      email: '',
      iliasName: '',

      //for the edit
      nameEdit: '',
      prenameEdit: '',
      emailEdit: '',
      iliasEdit: '',
      indexStudentEdit: 0,
      studentAlreadyExists: false,

      addForm: false,
      editForm: false,
      deleteForm: false,
    }
  },


  created: async function () {
    this.teamID = this.$route.params.team;
    await axios.get(`http://localhost:8080/api/v1/teams/` + this.teamID)
        .then((response) => {
          this.team = response.data
        })
    await this.fetchAllRegistrations()
    this.newNameProject = this.team.name

    await axios.get(`http://localhost:8080/api/v1/teams`)
        .then((response) => {
          this.allTeamsNames = response.data.map(x => x.name)
        })
  },


  methods: {
    fetchAllRegistrations: async function () {
      await axios.get(`http://localhost:8080/api/v1/submittedRegistrations`)
          .then(response => {
            this.registrationsStudents = response.data.map(registration => registration.students)
          })
          .catch((error) => {
            if (error.response) {
              this.notify("Projects Database Error", error.message, "danger")
            } else {
              this.notify("Projects Database Error", "Connection to Database Error", "danger")
            }
          })
    },

    validSubmission() {
      return (this.students.length !== 0 && this.preferedProjectIDs.length === this.projects.length)
    },

    saveChanges: function () {


      this.team.students[this.indexStudentEdit].prename = this.prenameEdit;
      this.team.students[this.indexStudentEdit].name = this.nameEdit;
      this.team.students[this.indexStudentEdit].iliasName = this.iliasEdit;
      this.team.students[this.indexStudentEdit].email = this.emailEdit;
    },
    getStudentData: function (id) {
      this.currentStudent = this.team.students[id]
      this.prenameEdit = this.team.students[id].prename;
      this.nameEdit = this.team.students[id].name;
      this.emailEdit = this.team.students[id].email;
      this.iliasEdit = this.team.students[id].iliasName;
      this.indexStudentEdit = id;


    },
    deleteStudent: function (student) {
      for (let i = 0; i < this.team.students.length; i++) {
        if (this.team.students[i].email === student.email) this.team.students.splice(i, 1)
        this.notify("Confirmation", 'Deletion was successful', "danger")
      }


    },

    validUpdate: function () {

      return (this.team.students.length !== 0 && this.newNameProject.length !== 0 &&
          !this.allTeamsNames.filter(name => name !== this.team.name).includes(this.newNameProject))
    },
    validStudent() {
      let studentAlreadyExists = false;
      for (let i = 0; i < this.team.students.length; i++) {
        if (this.team.students[i].iliasName === this.iliasName || this.team.students[i].email === this.email) {
          studentAlreadyExists = true;
          break;
        }
      }
      for (let j = 0; j < this.registrationsStudents.length; j++) {
        for (let i = 0; i < this.registrationsStudents[j].length; i++) {
          if (this.registrationsStudents[j][i].email === this.email || this.registrationsStudents[j][i].iliasName === this.iliasName) {

            studentAlreadyExists = true;
            break;
          }
        }
      }
      return (!studentAlreadyExists && this.email.endsWith("@stud.uni-stuttgart.de") && !this.email.startsWith("@") && this.prename.length !== 0
          && this.name.length !== 0 && this.iliasName.length !== 0 && this.iliasName.charAt(0) === this.iliasName.charAt(0).toUpperCase())
    },
    validStudentEdit() {
      let studentAlreadyExists = false;
      for (let i = 0; i < this.team.students.length; i++) {
        if (this.team.students[i].iliasName === this.iliasEdit || this.team.students[i].email === this.emailEdit) {
          studentAlreadyExists = true;
          break;
        }
      }
      for (let j = 0; j < this.registrationsStudents.length; j++) {
        for (let i = 0; i < this.registrationsStudents[j].length; i++) {
          if (this.registrationsStudents[j][i].email === this.emailEdit || this.registrationsStudents[j][i].iliasName === this.iliasEdit) {
            studentAlreadyExists = true;
            break;
          }
        }
      }
      if (this.currentStudent.email === this.emailEdit || this.currentStudent.iliasEdit === this.iliasEdit) studentAlreadyExists = false
      return (!studentAlreadyExists && this.emailEdit.endsWith("@stud.uni-stuttgart.de") && !this.emailEdit.startsWith("@") && this.prenameEdit.length !== 0
          && this.nameEdit.length !== 0 && this.iliasEdit.length !== 0 && this.iliasEdit.charAt(0) === this.iliasEdit.charAt(0).toUpperCase())
    },
    addStudent: function () {

      this.team.students = this.team.students.concat({
        "prename": this.prename,
        "name": this.name,
        "email": this.email,
        "iliasName": this.iliasName,
      })
      this.name = '';
      this.prename = '';
      this.email = '';
      this.iliasName = '';
      this.addForm = false;
    },
    /** Resets all values of input and edit fields. Also resets the values for the employee dropdown*/
    resetAllValues: function () {
      this.prename = '';
      this.name = '';
      this.email = '';
      this.iliasName = '';
      this.iliasEdit = '';
      this.nameEdit = '';
      this.prenameEdit = '';
      this.emailEdit = '';
    },
    /** Shows prompt with title, message and selected color*/
    notify: function (title, message, color) {
      this.$vs.notify({
        title: title,
        text: message,
        color: color, type: "gradient",
      })
    },

    updateTeam: async function () {
      await axios.put("http://localhost:8080/api/v1/teams/" + this.team.id, {
        "students": this.team.students,
        "name": this.newNameProject
      })
      this.notify("Confirmation", "Submission successfully performed", "success")
      this.$router.push({name: "Teams", params: {}});
    },


  }


}
</script>

<style>

</style>