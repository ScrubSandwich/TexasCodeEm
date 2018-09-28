import React, { Component } from 'react';

class Table extends Component {
  render() {
    const viewBox = [0, 0, window.innerWidth + 500, window.innerHeight];

    return (
      <div className="Table">
         <svg xmlns="http://www.w3.org/2000/svg" viewBox={viewBox}>
            <ellipse cx="2500" cy="1250"
            rx="2250" ry="1100"
            stroke="#05a912" stroke-width="15"
            fill="#05a912" />
          </svg>
      </div>
    );
  }
}

export default Table;
