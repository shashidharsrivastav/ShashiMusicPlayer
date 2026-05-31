import { displaySongs, highlightCurrentSong } from './songList.js';

let playPromise = null;
let currentTrackIndex = 0;
let trackList = [];
let filteredList = [];

let isShuffle = false;
let isRepeat = false;

const audio = document.getElementById('audio');
const stopBtn = document.getElementById('stop');
const nextBtn = document.getElementById('next');
const prevBtn = document.getElementById('previous');
const searchInput = document.getElementById('search');

const shuffleBtn = document.getElementById('shuffleBtn');
const repeatBtn = document.getElementById('repeatBtn');

async function fetchFiles() {
  try {
    const response = await fetch('http://192.168.29.164:8080/api/files');
    if (!response.ok) throw new Error('Failed to fetch files');
    return await response.json();
  } catch (error) {
    console.error('Error fetching files:', error);
    return [];
  }
}

export function loadTrack(index, autoplay = false) {
  if (!filteredList.length || index < 0 || index >= filteredList.length) return;

  currentTrackIndex = index;
  const fileName = filteredList[index];

  audio.pause();

  audio.src = `http://192.168.29.164:8080/api/music/${encodeURIComponent(fileName)}`;
  audio.load();

  const originalIndex = trackList.indexOf(fileName);
  highlightCurrentSong(originalIndex);

  if (autoplay) {
    playPromise = audio.play();

    if (playPromise !== undefined) {
      playPromise
        .then(() => {
          console.log("Playback started successfully!");
        })
        .catch((err) => {
          console.warn("Playback safely interrupted or handled:", err.message);
        });
    }
  }
}

shuffleBtn.addEventListener('click', () => {
  isShuffle = !isShuffle;
  shuffleBtn.textContent = `Shuffle: ${isShuffle ? 'ON' : 'OFF'}`;
  shuffleBtn.style.backgroundColor = isShuffle ? '#ff9800' : '#e0e0e0';
});

repeatBtn.addEventListener('click', () => {
  isRepeat = !isRepeat;
  repeatBtn.textContent = `Repeat: ${isRepeat ? 'ON' : 'OFF'}`;
  repeatBtn.style.backgroundColor = isRepeat ? '#ff9800' : '#e0e0e0';
});

function playNextTrack() {
  if (!filteredList.length) return;

  if (isRepeat) {
    loadTrack(currentTrackIndex, true);
  } else if (isShuffle && filteredList.length > 1) {
    let randomIndex;
    do {
      randomIndex = Math.floor(Math.random() * filteredList.length);
    } while (randomIndex === currentTrackIndex);
    currentTrackIndex = randomIndex;
    loadTrack(currentTrackIndex, true);
  } else {
    currentTrackIndex = (currentTrackIndex + 1) % filteredList.length;
    loadTrack(currentTrackIndex, true);
  }
}

stopBtn.addEventListener('click', () => {
  audio.pause();
  audio.currentTime = 0;
});

nextBtn.addEventListener('click', () => {
  playNextTrack();
});

prevBtn.addEventListener('click', () => {
  if (!filteredList.length) return;

  if (isShuffle && filteredList.length > 1) {
    let randomIndex = Math.floor(Math.random() * filteredList.length);
    currentTrackIndex = randomIndex;
  } else {
    currentTrackIndex = (currentTrackIndex - 1 + filteredList.length) % filteredList.length;
  }
  loadTrack(currentTrackIndex, true);
});

audio.addEventListener('timeupdate', () => {});

audio.addEventListener('ended', () => {
  playNextTrack();
});

searchInput.addEventListener('input', () => {
    const searchTerm = searchInput.value.toLowerCase().trim();
    const currentPlayingFile = filteredList[currentTrackIndex];

    filteredList = trackList.filter(track =>
      track.toLowerCase().includes(searchTerm)
    );

    displaySongs(filteredList, loadTrack);

    if (filteredList.length > 0) {
      const newIndex = filteredList.indexOf(currentPlayingFile);
      if (newIndex !== -1) {
        currentTrackIndex = newIndex;
        const originalIndex = trackList.indexOf(currentPlayingFile);
        highlightCurrentSong(originalIndex);
      } else {
        currentTrackIndex = 0;
      }
    }
});

window.addEventListener('DOMContentLoaded', async () => {
  trackList = await fetchFiles();
  filteredList = [...trackList];

  if (trackList.length > 0) {
    currentTrackIndex = 0;
    displaySongs(filteredList, loadTrack);
    highlightCurrentSong(currentTrackIndex);
    console.log("Player filtered updated smoothly!");
  } else {
    console.log("No songs found in the directory.");
  }
});