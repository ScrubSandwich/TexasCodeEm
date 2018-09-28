import React, { Component } from 'react';
import "./ButtonPanel.css"

class ButtonPanel extends Component {

  render() {
    return (
      <div className="ButtonPanel">
         <button>Check</button>
         <button>Raise</button>
         <button>Fold</button>
      </div>
    );
  }
}

export default ButtonPanel;
