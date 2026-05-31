    export function displaySongs(trackList, loadTrack) {
  const songsListContainer = document.getElementById('songList');
  if (!songsListContainer) return;
  songsListContainer.innerHTML = "";

  trackList.forEach((songName, index) => {
    const li = document.createElement('li');
    li.style.cursor = 'pointer';

    const img = document.createElement('img');
    img.classList.add('song-img');

    img.src = `http://192.168.29.164:8080/api/music/image/${encodeURIComponent(songName)}`;

    img.onerror = () => {
      img.src = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=200&auto=format&fit=crop";
    };
    img.alt = "album Art";

    const titleSpan = document.createElement('span');
    titleSpan.textContent = songName;
    titleSpan.classList.add('song-title');

    li.appendChild(img);
    li.appendChild(titleSpan);

    li.addEventListener('click', () => {
      loadTrack(index, true);
    });

    songsListContainer.appendChild(li);
  });
}

export function highlightCurrentSong(currentTrackIndex) {
  const songsListContainer = document.getElementById('songList');
  if (!songsListContainer) return;

  const allItems = songsListContainer.getElementsByTagName('li');
  for (let i = 0; i < allItems.length; i++) {
    const titleSpan = allItems[i].querySelector('.song-title');

    if (i === currentTrackIndex) {
      allItems[i].style.border = "2px solid orange";
      allItems[i].style.color = "orange";
      allItems[i].style.fontWeight = "bold";
      if (titleSpan) titleSpan.style.color = "orange";
    } else {
      allItems[i].style.border = "none";
      allItems[i].style.color = "#333333";
      allItems[i].style.fontWeight = "normal";
      if (titleSpan) titleSpan.style.color = "#333333";
    }
  }
}