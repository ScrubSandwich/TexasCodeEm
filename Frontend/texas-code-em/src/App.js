import React, { Component } from 'react';
import './App.css';
import Table from './Table';
import ButtonPanel from "./ButtonPanel"
import axios from "axios";

class App extends Component {
  render() {
    return (
      <div className="App">
        <Table />
      </div>
    );
  }

  componentWillMount = () => {
    console.log("Getting userId from backend...");
    const url = "http://localhost:8080/api/generateUserId";

    axios.post(url, {
      "test": "test"
    })
      .then(res => {
        console.log("User ID: " + res.data.userID);

        localStorage.setItem("userId", res.data.userID);
      })
  }
  
}

export default App;
